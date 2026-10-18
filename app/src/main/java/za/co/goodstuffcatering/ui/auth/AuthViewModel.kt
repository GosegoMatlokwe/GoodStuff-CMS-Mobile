package za.co.goodstuffcatering.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import za.co.goodstuffcatering.data.repository.AuthRepository
import javax.inject.Inject

/**
 * @HiltViewModel tells Hilt to manage the lifecycle of this ViewModel.
 * @Inject constructor allows Hilt to inject the AuthRepository automatically.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Get the instance of Firebase Auth
    private val auth = FirebaseAuth.getInstance()

    // MutableStateFlow holds the current state of the login process.
    // It starts as Idle.
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)

    // StateFlow is the read-only version exposed to the UI. The UI listens to this.
    val authState: StateFlow<AuthState> = _authState

    /**
     * Called when the user selects a Google account from the popup.
     * @param idToken The token provided by Google.
     */
    fun signInWithGoogle(idToken: String) {
        // Launch a coroutine so the network request doesn't freeze the UI
        viewModelScope.launch {
            _authState.value = AuthState.Loading // Show loading spinner
            try {
                // 1. Create Firebase credential using the Google ID Token
                val credential = GoogleAuthProvider.getCredential(idToken, null)

                // 2. Sign into Firebase
                val authResult = auth.signInWithCredential(credential).await()
                val user = authResult.user

                // 3. Extract user data from Firebase
                val email = user?.email ?: ""
                val name = user?.displayName ?: ""

                // 4. Sync with Django backend.
                // NOTE: We removed the token from this call because AuthInterceptor does it automatically!
                val result = authRepository.syncUser(email, name)

                // 5. Update the state based on the result
                if (result.isSuccess) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Backend sync failed")
                }

            } catch (e: Exception) {
                // Catch any crash (e.g., no internet) and show an error message
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }
}

/**
 * Represents the possible states of the Authentication process.
 */
sealed class AuthState {
    object Idle : AuthState()      // Doing nothing
    object Loading : AuthState()   // Currently signing in
    object Success : AuthState()   // Successfully signed in
    data class Error(val message: String) : AuthState() // Failed with a message
}

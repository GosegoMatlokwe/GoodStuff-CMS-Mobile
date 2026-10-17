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

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(credential).await()
                val user = authResult.user

                val firebaseToken = user?.getIdToken(false)?.await()?.token
                val email = user?.email ?: ""
                val name = user?.displayName ?: ""

                if (firebaseToken != null) {
                    // Sync with Django backend
                    val result = authRepository.syncUser(firebaseToken, email, name)
                    if (result.isSuccess) {
                        _authState.value = AuthState.Success
                    } else {
                        _authState.value = AuthState.Error("Backend sync failed")
                    }
                } else {
                    _authState.value = AuthState.Error("Failed to get token")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun completeRegistration(phoneNumber: String, consentGiven: Boolean) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            val user = FirebaseAuth.getInstance().currentUser
            val token = user?.getIdToken(false)?.await()?.token
            val email = user?.email ?: ""
            val name = user?.displayName ?: "New User"

            if (token != null) {
                val result = authRepository.registerUser(
                    token = token,
                    email = email,
                    name = name,
                    phone = phoneNumber,
                    popiaConsent = consentGiven
                )
                if (result.isSuccess) {
                    _registerState.value = RegisterState.Success
                } else {
                    _registerState.value = RegisterState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
                }
            } else {
                _registerState.value = RegisterState.Error("Authentication error")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

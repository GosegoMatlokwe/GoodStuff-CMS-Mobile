package za.co.goodstuffcatering.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import za.co.goodstuffcatering.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _deleteState = MutableStateFlow<DeleteState>(DeleteState.Idle)
    val deleteState: StateFlow<DeleteState> = _deleteState

    fun deleteAccount() {
        viewModelScope.launch {
            _deleteState.value = DeleteState.Loading

            // Call repository (AuthInterceptor automatically adds the token!)
            val result = authRepository.deleteAccount()

            if (result.isSuccess) {
                // If Django deleted the user, sign out of Firebase too
                FirebaseAuth.getInstance().signOut()
                _deleteState.value = DeleteState.Success
            } else {
                _deleteState.value = DeleteState.Error("Failed to delete account")
            }
        }
    }
}

sealed class DeleteState {
    object Idle : DeleteState()
    object Loading : DeleteState()
    object Success : DeleteState()
    data class Error(val message: String) : DeleteState()
}
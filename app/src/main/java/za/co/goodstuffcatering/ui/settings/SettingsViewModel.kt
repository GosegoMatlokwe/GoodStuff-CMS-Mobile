package za.co.goodstuffcatering.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import za.co.goodstuffcatering.data.remote.ApiService
import za.co.goodstuffcatering.data.remote.dto.UpdateProfileRequest
import za.co.goodstuffcatering.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val apiService: ApiService // Injected to save profile
) : ViewModel() {

    private val _deleteState = MutableStateFlow<DeleteState>(DeleteState.Idle)
    val deleteState: StateFlow<DeleteState> = _deleteState

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState

    fun updateProfile(firstName: String, lastName: String, phone: String) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            try {
                // Note: AuthInterceptor adds the token automatically
                val response = apiService.updateProfile(UpdateProfileRequest(firstName, lastName, phone))
                if (response.isSuccessful) {
                    _updateState.value = UpdateState.Success
                } else {
                    _updateState.value = UpdateState.Error("Failed to update profile")
                }
            } catch (e: Exception) {
                _updateState.value = UpdateState.Error(e.message ?: "Network error")
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _deleteState.value = DeleteState.Loading
            val result = authRepository.deleteAccount()
            if (result.isSuccess) {
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

sealed class UpdateState {
    object Idle : UpdateState()
    object Loading : UpdateState()
    object Success : UpdateState()
    data class Error(val message: String) : UpdateState()
}
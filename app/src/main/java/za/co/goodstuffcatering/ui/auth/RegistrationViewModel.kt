package za.co.goodstuffcatering.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import za.co.goodstuffcatering.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _popiaConsent = MutableStateFlow(false)
    val popiaConsent: StateFlow<Boolean> = _popiaConsent

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun onPhoneChanged(phone: String) {
        _phoneNumber.value = phone
    }

    fun onConsentChanged(consent: Boolean) {
        _popiaConsent.value = consent
    }

    fun submitRegistration() {
        if (!_popiaConsent.value) {
            _registerState.value = RegisterState.Error("You must accept the POPIA terms to register.")
            return
        }
        if (_phoneNumber.value.length < 10) {
            _registerState.value = RegisterState.Error("Please enter a valid SA phone number.")
            return
        }

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val user = FirebaseAuth.getInstance().currentUser
            val token = user?.getIdToken(false)?.await()?.token

            if (token != null) {
                val result = authRepository.registerUser(
                    token = token,
                    email = user.email ?: "",
                    name = user.displayName ?: "New User",
                    phone = _phoneNumber.value,
                    popiaConsent = true
                )
                _registerState.value = if (result.isSuccess) {
                    RegisterState.Success
                } else {
                    RegisterState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
                }
            } else {
                _registerState.value = RegisterState.Error("Authentication error")
            }
        }
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

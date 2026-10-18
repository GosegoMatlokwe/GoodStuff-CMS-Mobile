package za.co.goodstuffcatering.ui.auth

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
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Holds the phone number typed by the user
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    // Holds whether the POPIA checkbox is ticked (defaults to false)
    private val _popiaConsent = MutableStateFlow(false)
    val popiaConsent: StateFlow<Boolean> = _popiaConsent

    // Holds the state of the registration (Idle, Loading, Success, Error)
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    // Updates the phone number state when the user types
    fun onPhoneChanged(phone: String) {
        _phoneNumber.value = phone
    }

    // Updates the consent state when the user clicks the checkbox
    fun onConsentChanged(consent: Boolean) {
        _popiaConsent.value = consent
    }

    /**
     * Triggered when the user taps "Complete Registration".
     */
    fun submitRegistration() {
        // 1. Client-side validation (Prevents sending bad data to Django)
        if (!_popiaConsent.value) {
            _registerState.value = RegisterState.Error("You must accept the POPIA terms to register.")
            return // Stop execution here
        }
        if (_phoneNumber.value.length < 10) {
            _registerState.value = RegisterState.Error("Please enter a valid SA phone number.")
            return // Stop execution here
        }

        // 2. Proceed with registration
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                // NOTE: Removed the token parameter here as well (AuthInterceptor handles it)
                val result = authRepository.registerUser(
                    email = user.email ?: "",
                    name = user.displayName ?: "New User",
                    phone = _phoneNumber.value,
                    popiaConsent = true
                )

                // Update UI state based on success or failure
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

// Represents the possible states of the Registration process.
sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}
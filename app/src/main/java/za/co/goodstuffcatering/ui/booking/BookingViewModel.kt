package za.co.goodstuffcatering.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import za.co.goodstuffcatering.data.remote.dto.BookingRequest
import za.co.goodstuffcatering.data.repository.BookingRepository
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _guestCount = MutableStateFlow("")
    val guestCount: StateFlow<String> = _guestCount

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting

    private val _submitSuccess = MutableStateFlow(false)
    val submitSuccess: StateFlow<Boolean> = _submitSuccess

    fun onGuestCountChanged(input: String) {
        _guestCount.value = input
        validateGuestCount(input)
    }

    private fun validateGuestCount(input: String) {
        if (input.isEmpty()) {
            _errorMessage.value = "Guest count cannot be empty"
            return
        }
        val count = input.toIntOrNull()
        if (count == null) {
            _errorMessage.value = "Please enter a valid number"
        } else if (count < 10) {
            _errorMessage.value = "Minimum booking is 10 guests"
        } else if (count > 500) {
            _errorMessage.value = "For events over 500, contact us directly"
        } else {
            _errorMessage.value = null
        }
    }

    fun submitBooking(eventType: String, eventDate: String) {
        viewModelScope.launch {
            _isSubmitting.value = true
            val token = FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.await()?.token
            if (token != null && _errorMessage.value == null && _guestCount.value.isNotEmpty()) {
                val request = BookingRequest(
                    eventDate = eventDate,
                    eventType = eventType,
                    guestCount = _guestCount.value.toInt(),
                    menuItems = emptyList(),
                    staffNeeded = 5
                )
                val result = bookingRepository.createBooking(token, request)
                if (result.isSuccess) {
                    _submitSuccess.value = true
                } else {
                    _errorMessage.value = "Submission failed. Try again."
                }
            } else {
                _errorMessage.value = "Please fix errors before submitting."
            }
            _isSubmitting.value = false
        }
    }
}
package za.co.goodstuffcatering.ui.booking

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor() : ViewModel() {

    private val _guestCount = MutableStateFlow("")
    val guestCount: StateFlow<String> = _guestCount

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

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
            _errorMessage.value = "Minimum booking is 10 guests for catering"
        } else if (count > 500) {
            _errorMessage.value = "For events over 500, please contact us directly"
        } else {
            _errorMessage.value = null // Valid input
        }
    }
}
package za.co.goodstuffcatering.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor() : ViewModel() {

    // Holds the result of the payment (Success or Error)
    private val _paymentResult = MutableStateFlow<PaymentResult?>(null)
    val paymentResult: StateFlow<PaymentResult?> = _paymentResult

    /**
     * Simulates processing a deposit payment.
     * In a real production app, this would connect to the Peach Payments SDK.
     */
    fun processDeposit(amountInZAR: Double, bookingId: String) {
        viewModelScope.launch {
            // 1. Simulate a network delay to show the UI loading spinner
            delay(2000) // Wait for 2 seconds

            // 2. Simulate a successful payment response from the server
            // In production: You would make an API call to Django, which returns a Peach checkout ID.
            _paymentResult.value = PaymentResult.Success(
                transactionId = "PEACH-TXN-${System.currentTimeMillis()}",
                message = "Deposit of R$amountInZAR paid successfully for Booking #$bookingId."
            )
        }
    }
}

/**
 * Sealed class representing the outcome of a payment transaction.
 */
sealed class PaymentResult {
    data class Success(val transactionId: String, val message: String) : PaymentResult()
    data class Error(val message: String) : PaymentResult()
}
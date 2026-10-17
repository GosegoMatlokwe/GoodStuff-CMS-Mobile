package za.co.goodstuffcatering.ui.payment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor() : ViewModel() {

    private val _paymentResult = MutableStateFlow<PaymentResult?>(null)
    val paymentResult: StateFlow<PaymentResult?> = _paymentResult

    fun processDeposit(amountInZAR: Double, bookingId: String) {
        // 1. In a real app, you call your Django backend here.
        // 2. Django securely requests a checkout ID from Peach Payments.
        // 3. Django returns the checkout ID to Kotlin.
        // 4. Kotlin opens the Peach Payments SDK with that ID.

        // Simulating a successful deposit payment flow
        _paymentResult.value = PaymentResult.Success(
            transactionId = "PEACH-TXN-${System.currentTimeMillis()}",
            message = "Deposit of R$amountInZAR paid successfully."
        )
    }
}

sealed class PaymentResult {
    data class Success(val transactionId: String, val message: String) : PaymentResult()
    data class Error(val message: String) : PaymentResult()
}
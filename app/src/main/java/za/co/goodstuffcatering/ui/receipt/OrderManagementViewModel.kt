package za.co.goodstuffcatering.ui.receipt

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import za.co.goodstuffcatering.data.local.BookingDao
import za.co.goodstuffcatering.data.local.BookingEntity
import za.co.goodstuffcatering.utils.ReceiptGenerator
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OrderManagementViewModel @Inject constructor(
    private val bookingDao: BookingDao
) : ViewModel() {

    // Holds the specific booking fetched by ID
    private val _booking = MutableStateFlow<BookingEntity?>(null)
    val booking: StateFlow<BookingEntity?> = _booking

    private val _generatedReceipt = MutableStateFlow<File?>(null)
    val generatedReceipt: StateFlow<File?> = _generatedReceipt

    // Fetch the booking from Room database
    fun loadBooking(bookingId: Int) {
        viewModelScope.launch {
            _booking.value = bookingDao.getBookingById(bookingId)
        }
    }

    // Generate the receipt using the real data
    fun generateReceiptForOrder(context: Context) {
        val currentBooking = _booking.value ?: return
        viewModelScope.launch {
            val file = ReceiptGenerator.generateReceipt(
                context = context,
                invoiceNumber = "INV-${currentBooking.id}", // Use the real DB ID
                customerName = "Valued Customer", // Ideally pull from Firebase Auth
                eventType = currentBooking.eventType,
                eventDate = currentBooking.eventDate,
                totalAmount = currentBooking.totalCost // Use the real cost
            )
            _generatedReceipt.value = file
        }
    }
}
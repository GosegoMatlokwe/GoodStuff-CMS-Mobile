package za.co.goodstuffcatering.ui.receipt

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import za.co.goodstuffcatering.data.local.BookingDao
import za.co.goodstuffcatering.data.local.OrderEntity
import za.co.goodstuffcatering.utils.ReceiptGenerator
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OrderManagementViewModel @Inject constructor(
    private val bookingDao: BookingDao
) : ViewModel() {

    // Fetch orders from Room DB and expose them as a StateFlow for the UI
    val orders: StateFlow<List<OrderEntity>> = bookingDao.getOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Holds the generated PDF file
    private val _generatedReceipt = MutableStateFlow<File?>(null)
    val generatedReceipt: StateFlow<File?> = _generatedReceipt

    // Triggers the PDF generation logic
    fun generateReceiptForOrder(context: Context, order: OrderEntity) {
        viewModelScope.launch {
            val file = ReceiptGenerator.generateReceipt(
                context = context,
                invoiceNumber = order.serverId,
                customerName = "Gosego Matlokwe", // In production, fetch from Auth state
                eventType = order.eventType,
                eventDate = order.eventDate,
                totalAmount = order.totalCost
            )
            _generatedReceipt.value = file // Trigger the UI to open the PDF
        }
    }
}
package za.co.goodstuffcatering.ui.receipt

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import za.co.goodstuffcatering.utils.ReceiptGenerator
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor() : ViewModel() {

    private val _receiptFile = MutableStateFlow<File?>(null)
    val receiptFile: StateFlow<File?> = _receiptFile

    fun generatePdfReceipt(
        context: Context,
        invoiceNumber: String,
        customerName: String,
        eventType: String,
        eventDate: String,
        totalAmount: Double
    ) {
        val file = ReceiptGenerator.generateReceipt(
            context = context,
            invoiceNumber = invoiceNumber,
            customerName = customerName,
            eventType = eventType,
            eventDate = eventDate,
            totalAmount = totalAmount
        )
        _receiptFile.value = file
    }
}
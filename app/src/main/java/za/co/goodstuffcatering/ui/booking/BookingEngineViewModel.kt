package za.co.goodstuffcatering.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import za.co.goodstuffcatering.data.local.BookingDao
import za.co.goodstuffcatering.data.local.OrderEntity
import za.co.goodstuffcatering.domain.usecase.CalculateQuoteUseCase
import za.co.goodstuffcatering.domain.usecase.QuoteResult
import javax.inject.Inject

@HiltViewModel
class BookingEngineViewModel @Inject constructor(
    private val calculateQuoteUseCase: CalculateQuoteUseCase,
    private val bookingDao: BookingDao
) : ViewModel() {

    private val _guestCount = MutableStateFlow(10)
    val guestCount: StateFlow<Int> = _guestCount

    private val _venueAddress = MutableStateFlow("")
    val venueAddress: StateFlow<String> = _venueAddress

    // This will hold the calculated total (Food + Delivery + VAT)
    private val _currentQuote = MutableStateFlow<QuoteResult?>(null)
    val currentQuote: StateFlow<QuoteResult?> = _currentQuote

    private var simulatedDistanceKm = 20.0 // Normally retrieved from Google Distance Matrix
    private var pricePerHead = 150.0 // Normally retrieved from selected Menu Item

    fun updateGuestCount(count: Int) {
        _guestCount.value = count
        recalculateQuote()
    }

    fun updateAddress(address: String) {
        _venueAddress.value = address
        // In a real scenario, you would call Distance Matrix API here to get real distance
        recalculateQuote()
    }

    private fun recalculateQuote() {
        val quote = calculateQuoteUseCase.execute(
            basePricePerHead = pricePerHead,
            guestCount = _guestCount.value,
            distanceKm = simulatedDistanceKm
        )
        _currentQuote.value = quote
    }

    fun saveOrderDraft() {
        viewModelScope.launch {
            val quote = _currentQuote.value ?: return@launch
            val order = OrderEntity(
                serverId = "draft_${System.currentTimeMillis()}",
                eventDate = "2026/06/05", // Should come from a date picker
                eventType = "Wedding",
                guestCount = _guestCount.value,
                venueAddress = _venueAddress.value,
                deliveryFee = quote.deliveryFee,
                totalCost = quote.totalCost,
                status = "Draft"
            )
            bookingDao.insertOrder(order)
        }
    }
}
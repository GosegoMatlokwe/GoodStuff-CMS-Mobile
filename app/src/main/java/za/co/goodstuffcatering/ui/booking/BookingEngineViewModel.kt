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
    // Injects the UseCase that handles the VAT and Delivery math
    private val calculateQuoteUseCase: CalculateQuoteUseCase,
    // Injects the database to save offline drafts
    private val bookingDao: BookingDao
) : ViewModel() {

    // Holds the number of guests (starts at 10)
    private val _guestCount = MutableStateFlow(10)
    val guestCount: StateFlow<Int> = _guestCount

    // Holds the address string
    private val _venueAddress = MutableStateFlow("")
    val venueAddress: StateFlow<String> = _venueAddress

    // Holds the calculated quote breakdown. It's nullable (?) because it might not be calculated yet.
    private val _currentQuote = MutableStateFlow<QuoteResult?>(null)
    val currentQuote: StateFlow<QuoteResult?> = _currentQuote

    // Hardcoded values for prototype testing
    private var simulatedDistanceKm = 20.0
    private var pricePerHead = 150.0

    /**
     * Updates the guest count and automatically recalculates the quote.
     */
    fun updateGuestCount(count: Int) {
        _guestCount.value = count
        recalculateQuote()
    }

    /**
     * Updates the address and recalculates the quote.
     */
    fun updateAddress(address: String) {
        _venueAddress.value = address
        // In production, you would call Google Maps API here to get the REAL distance
        recalculateQuote()
    }

    /**
     * Calls the Domain UseCase to do the math and updates the StateFlow.
     */
    private fun recalculateQuote() {
        val quote = calculateQuoteUseCase.execute(
            basePricePerHead = pricePerHead,
            guestCount = _guestCount.value,
            distanceKm = simulatedDistanceKm
        )
        _currentQuote.value = quote
    }

    /**
     * Saves the current booking as a draft in the Room database (Offline-first).
     */
    fun saveOrderDraft() {
        viewModelScope.launch {
            // If the quote hasn't been calculated, don't save.
            val quote = _currentQuote.value ?: return@launch

            val order = OrderEntity(
                serverId = "draft_${System.currentTimeMillis()}", // Temporary ID
                eventDate = "2026/06/05",
                eventType = "Wedding",
                guestCount = _guestCount.value,
                venueAddress = _venueAddress.value,
                deliveryFee = quote.deliveryFee,
                totalCost = quote.totalCost,
                status = "Draft"
            )
            // Inserts the draft into the local database
            bookingDao.insertOrder(order)
        }
    }
}
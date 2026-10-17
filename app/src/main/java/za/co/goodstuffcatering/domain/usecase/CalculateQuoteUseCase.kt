package za.co.goodstuffcatering.domain.usecase

import javax.inject.Inject

class CalculateQuoteUseCase @Inject constructor() {

    /**
     * Calculates the total cost of a booking.
     * @param basePricePerHead The cost of the selected menu items per person.
     * @param guestCount Number of guests.
     * @param distanceKm Distance from kitchen to venue (from Google Distance Matrix).
     */
    fun execute(
        basePricePerHead: Double,
        guestCount: Int,
        distanceKm: Double
    ): QuoteResult {
        val foodTotal = basePricePerHead * guestCount

        // Delivery Fee Logic (Free within 15km, R5/km thereafter)
        val freeRadiusKm = 15.0
        val costPerKm = 5.0
        val deliveryFee = if (distanceKm > freeRadiusKm) {
            (distanceKm - freeRadiusKm) * costPerKm
        } else {
            0.0
        }

        // 15% VAT Calculation (South Africa)
        val subTotal = foodTotal + deliveryFee
        val vat = subTotal * 0.15
        val totalCost = subTotal + vat

        return QuoteResult(
            foodTotal = foodTotal,
            deliveryFee = deliveryFee,
            vatAmount = vat,
            totalCost = totalCost
        )
    }
}

data class QuoteResult(
    val foodTotal: Double,
    val deliveryFee: Double,
    val vatAmount: Double,
    val totalCost: Double
)
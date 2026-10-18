package za.co.goodstuffcatering.domain.usecase

import javax.inject.Inject

/**
 * UseCase: Calculates the dynamic quote for a booking.
 * @Inject constructor allows Hilt to provide this class where needed.
 */
class CalculateQuoteUseCase @Inject constructor() {

    /**
     * Executes the calculation logic.
     * @param basePricePerHead The cost of the food per person.
     * @param guestCount The number of people attending.
     * @param distanceKm The distance from the kitchen to the venue.
     * @return QuoteResult containing the detailed breakdown.
     */
    fun execute(
        basePricePerHead: Double,
        guestCount: Int,
        distanceKm: Double
    ): QuoteResult {
        // 1. Prevent negative numbers from breaking the math
        val safeGuestCount = if (guestCount > 0) guestCount else 0
        val safeDistance = if (distanceKm > 0) distanceKm else 0.0
        val safePrice = if (basePricePerHead > 0) basePricePerHead else 0.0

        // 2. Calculate the cost of food
        val foodTotal = safePrice * safeGuestCount

        // 3. Delivery Fee Logic (Free within 15km, R5.00 per km thereafter)
        val freeRadiusKm = 15.0
        val costPerKm = 5.0

        // If the distance is greater than 15km, calculate the extra fee
        val deliveryFee = if (safeDistance > freeRadiusKm) {
            (safeDistance - freeRadiusKm) * costPerKm
        } else {
            0.0 // Free delivery
        }

        // 4. 15% VAT Calculation (South African Standard)
        // We add the food and delivery first, then calculate VAT on the total
        val subTotal = foodTotal + deliveryFee
        val vat = subTotal * 0.15

        // 5. Final total
        val totalCost = subTotal + vat

        // Return the breakdown so the UI can display it to the user
        return QuoteResult(
            foodTotal = foodTotal,
            deliveryFee = deliveryFee,
            vatAmount = vat,
            totalCost = totalCost
        )
    }
}

/**
 * Data class to hold the result of the calculation.
 */
data class QuoteResult(
    val foodTotal: Double,
    val deliveryFee: Double,
    val vatAmount: Double,
    val totalCost: Double
)
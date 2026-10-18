package za.co.goodstuffcatering.domain.model

/**
 * Domain Model: The pure, core representation of a Booking.
 * We keep this separate from the database entity (BookingEntity) and API DTOs
 * so that if the database changes, it doesn't break the UI logic.
 */
data class Booking(
    val id: Int,
    val eventDate: String,
    val eventType: String,
    val guestCount: Int,
    val status: String,
    val venueAddress: String = "",
    val totalCost: Double = 0.0
)
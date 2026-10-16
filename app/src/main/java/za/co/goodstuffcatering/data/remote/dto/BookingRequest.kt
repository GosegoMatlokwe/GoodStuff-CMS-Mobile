package za.co.goodstuffcatering.data.remote.dto

data class BookingRequest(
    val eventDate: String,
    val eventType: String,
    val guestCount: Int,
    val menuItems: List<String>,
    val staffNeeded: Int
)
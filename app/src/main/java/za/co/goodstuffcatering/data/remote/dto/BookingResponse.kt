package za.co.goodstuffcatering.data.remote.dto

data class BookingResponse(
    val id: Int,
    val eventDate: String,
    val eventType: String,
    val status: String
)
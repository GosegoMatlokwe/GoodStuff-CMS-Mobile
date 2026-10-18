package za.co.goodstuffcatering.data.remote.dto

/**
 * DTO used when Django sends booking data BACK to the Android app.
 * This matches the JSON structure returned by your Django API.
 */
data class BookingResponse(
    // The unique ID of the booking from the Django database
    val id: Int,
    // The date of the event
    val eventDate: String,
    // The type of event
    val eventType: String,
    // The current status (e.g., "Pending", "Confirmed")
    val status: String
)
package za.co.goodstuffcatering.data.remote.dto

/**
 * DTO used when the user submits a new booking from the Android app.
 * Retrofit will convert this object into a JSON payload to send to Django.
 */
data class BookingRequest(
    // The date of the event (formatted as YYYY-MM-DD)
    val eventDate: String,
    // The type of event (e.g., "Wedding", "Corporate")
    val eventType: String,
    // The number of people attending
    val guestCount: Int,
    // A list of menu items the user selected
    val menuItems: List<String>,
    // The number of staff members required for the event
    val staffNeeded: Int
)
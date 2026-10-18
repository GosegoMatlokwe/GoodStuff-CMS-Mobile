package za.co.goodstuffcatering.data.remote.dto

/**
 * DTO for POPIA compliance.
 * Sent to Django to legally record that the user consented to data processing.
 */
data class ConsentRequest(
    // The user's email
    val email: String,
    // Boolean flag indicating if they checked the box (should be true)
    val consentGiven: Boolean,
    // The exact time they checked the box (Unix timestamp)
    val timestamp: Long
)
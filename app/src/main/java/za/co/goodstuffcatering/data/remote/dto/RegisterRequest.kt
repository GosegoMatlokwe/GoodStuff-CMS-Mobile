package za.co.goodstuffcatering.data.remote.dto

/**
 * DTO sent to Django after a user successfully signs up via Google SSO.
 * This completes their profile and records their POPIA consent.
 */
data class RegisterRequest(
    val email: String,
    val name: String,
    // The phone number entered on the Registration Screen
    val phoneNumber: String,
    // True if they checked the POPIA box
    val popiaConsent: Boolean,
    // The green line is a warning that this default value is calculated when the object is created.
    // This is exactly what we want! It grabs the exact millisecond the registration happens.
    val consentTimestamp: Long = System.currentTimeMillis()
)
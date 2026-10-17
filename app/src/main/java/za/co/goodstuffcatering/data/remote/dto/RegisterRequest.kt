package za.co.goodstuffcatering.data.remote.dto

data class RegisterRequest(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val popiaConsent: Boolean,
    val consentTimestamp: Long = System.currentTimeMillis()
)
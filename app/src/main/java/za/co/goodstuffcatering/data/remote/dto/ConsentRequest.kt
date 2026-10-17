package za.co.goodstuffcatering.data.remote.dto

data class ConsentRequest(
    val email: String,
    val consentGiven: Boolean,
    val timestamp: Long
)
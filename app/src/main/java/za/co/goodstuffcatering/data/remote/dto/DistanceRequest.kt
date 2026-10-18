package za.co.goodstuffcatering.data.remote.dto

/**
 * DTO sent to Django to calculate travel distance for delivery fees.
 */
data class DistanceRequest(
    // The starting address (e.g., the catering kitchen)
    val origin: String,
    // The destination address (e.g., the wedding venue)
    val destination: String
)

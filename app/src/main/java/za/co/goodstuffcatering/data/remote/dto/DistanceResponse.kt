package za.co.goodstuffcatering.data.remote.dto

/**
 * DTO received from Django after a distance calculation.
 */
data class DistanceResponse(
    // The distance in kilometers
    val distanceKm: Double,
    // The estimated driving time (optional, so it can be null)
    val durationMinutes: Double? = null
)
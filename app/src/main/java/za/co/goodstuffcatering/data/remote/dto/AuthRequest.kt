package za.co.goodstuffcatering.data.remote.dto

/**
 * Data Transfer Object (DTO) for syncing a user.
 * This class is sent to the Django backend to link a Firebase user to a PostgreSQL user.
 */
data class AuthRequest(
    // The user's email address from Firebase
    val email: String,
    // The user's display name from Firebase
    val name: String
)
package za.co.goodstuffcatering.data.remote.dto

data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
)
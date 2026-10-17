package za.co.goodstuffcatering.data.remote.dto

data class OrderResponse(
    val orderId: String,
    val status: String,
    val totalAmount: Double
)

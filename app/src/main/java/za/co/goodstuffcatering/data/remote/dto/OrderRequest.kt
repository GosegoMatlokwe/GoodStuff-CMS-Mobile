package za.co.goodstuffcatering.data.remote.dto

/**
 * DTO for submitting a detailed order (used for checkout/payments).
 */
data class OrderRequest(
    // List of items being ordered
    val items: List<String>,
    // Total calculated amount
    val totalAmount: Double,
    // Where the food should be delivered
    val deliveryAddress: String? = null
)
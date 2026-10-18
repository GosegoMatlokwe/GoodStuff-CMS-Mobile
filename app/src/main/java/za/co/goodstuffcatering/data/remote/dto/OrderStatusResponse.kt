package za.co.goodstuffcatering.data.remote.dto

/**
 * DTO used to check the live status of an existing order.
 */
data class OrderStatusResponse(
    val orderId: String,
    // Updates as the order progresses (e.g., "Draft", "Deposit Paid", "Out for Delivery")
    val status: String
)

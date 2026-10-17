package za.co.goodstuffcatering.data.remote.dto

data class OrderRequest(
    val items: List<String>,
    val totalAmount: Double,
    val deliveryAddress: String? = null
)

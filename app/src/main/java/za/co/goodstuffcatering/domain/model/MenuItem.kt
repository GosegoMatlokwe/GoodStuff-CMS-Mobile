package za.co.goodstuffcatering.domain.model

/**
 * Domain Model representing a single item on the catering menu.
 */
data class MenuItem(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String
)
package za.co.goodstuffcatering.data.remote.dto

/**
 * DTO representing a single food item on the catering menu.
 * This is used in the Menu Discovery screen.
 */
data class MenuItemDto(
    // The unique database ID of the item
    val id: Int,
    // The name of the dish (e.g., "Beef Stew")
    val itemName: String,
    // The price per head
    val price: Double
)
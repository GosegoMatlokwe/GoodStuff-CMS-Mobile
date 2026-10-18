package za.co.goodstuffcatering.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents the "menu_items" table in the local Room database.
 * This mirrors the menu data coming from your Django backend.
 */
@Entity(tableName = "menu_items")
data class MenuItemEntity(
    // The unique ID of the menu item (from Django).
    @PrimaryKey val id: Int,
    // The name of the dish (e.g., "Beef Stew").
    val itemName: String,
    // The price per head. Double is used for decimals.
    val price: Double,
    // Category for filtering (e.g., "Weddings", "Corporate").
    val category: String,
    // Boolean to check if the item is currently in stock/available.
    val isAvailable: Boolean
)
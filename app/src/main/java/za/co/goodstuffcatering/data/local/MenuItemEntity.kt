package za.co.goodstuffcatering.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @PrimaryKey val id: Int,
    val itemName: String,
    val price: Double,
    val category: String, // e.g., "Weddings", "Corporate", "Vegan"
    val isAvailable: Boolean
)
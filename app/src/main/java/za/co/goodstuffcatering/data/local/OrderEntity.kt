package za.co.goodstuffcatering.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents the "orders" table. Used for saving drafts or offline orders.
 */
@Entity(tableName = "orders")
data class OrderEntity(
    // autoGenerate = true means Room will automatically assign an ID (1, 2, 3...) if we don't provide one.
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0,
    // The ID from the Django server once the order is synced.
    val serverId: String,
    val eventDate: String,
    val eventType: String,
    val guestCount: Int,
    val venueAddress: String,
    // The calculated delivery fee based on distance.
    val deliveryFee: Double,
    // The total cost including VAT and delivery.
    val totalCost: Double,
    // Tracks the lifecycle of the order (e.g., Draft, Confirmed).
    val status: String,
    // When the order was created locally.
    val createdAt: Long = System.currentTimeMillis()
)
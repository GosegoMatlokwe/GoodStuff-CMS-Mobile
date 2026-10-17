package za.co.goodstuffcatering.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0,
    val serverId: String, // ID from Django
    val eventDate: String,
    val eventType: String,
    val guestCount: Int,
    val venueAddress: String,
    val deliveryFee: Double,
    val totalCost: Double,
    val status: String, // Draft, Deposit Paid, Menu Locked, Out for Delivery
    val createdAt: Long = System.currentTimeMillis()
)
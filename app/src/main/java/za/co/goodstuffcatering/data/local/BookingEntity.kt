package za.co.goodstuffcatering.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: Int,
    val eventDate: String,
    val eventType: String,
    val status: String,
    val lastUpdated: Long = System.currentTimeMillis()
)
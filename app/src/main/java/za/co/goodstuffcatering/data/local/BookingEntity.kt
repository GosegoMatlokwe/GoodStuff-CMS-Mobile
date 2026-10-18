package za.co.goodstuffcatering.data.local

// Imports for Room Entity annotations
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The @Entity annotation tells Room to create a table named "bookings".
 * Each property in this data class becomes a column in the table.
 */
@Entity(tableName = "bookings")
data class BookingEntity(
    // @PrimaryKey means this column uniquely identifies the row. 'id' matches Django's ID.
    @PrimaryKey val id: Int,
    // Column for the event date (stored as a String, e.g., "2026-06-05")
    val eventDate: String,
    // Column for the event type (e.g., "Wedding")
    val eventType: String,
    // Column for the status (e.g., "Pending", "Confirmed")
    val status: String,
    // Column for tracking when this record was last updated locally.
    val guestCount: Int = 0,     // NEW
    val totalCost: Double = 0.0, // NEW
    // System.currentTimeMillis() defaults to the current time when the object is created.
    val lastUpdated: Long = System.currentTimeMillis()
)
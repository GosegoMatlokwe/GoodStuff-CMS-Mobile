package za.co.goodstuffcatering.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookingEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao
}

@Database(
    entities = [BookingEntity::class, MenuItemEntity::class, OrderEntity::class],
    version = 2, // Bump version
    exportSchema = false
)
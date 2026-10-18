// Defines the package location
package za.co.goodstuffcatering.data.local

// Imports for Room Database annotations and classes
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The @Database annotation tells Room that this class represents the entire database.
 * @param entities: A list of all the tables (Entities) in this database. We have three.
 * @param version: The current version of the database schema. Increment this (e.g., to 3) if you change a table.
 * @param exportSchema: If true, Room exports the schema to a JSON file for version control.
 */
@Database(
    entities = [BookingEntity::class, MenuItemEntity::class, OrderEntity::class],
    version = 2,
    exportSchema = false
)
// We extend RoomDatabase, which provides all the underlying SQLite functionality.
abstract class AppDatabase : RoomDatabase() {
    // This abstract function tells Room to automatically generate the code to access the BookingDao.
    // You will never write the implementation for this; Room does it for you behind the scenes.
    abstract fun bookingDao(): BookingDao
}


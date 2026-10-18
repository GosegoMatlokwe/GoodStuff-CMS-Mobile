package za.co.goodstuffcatering.data.local

// Imports for Room annotations and Coroutine Flow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * The @Dao annotation tells Room to generate the SQL code for these functions.
 * Because it's an interface, we don't write the function bodies; Room does it for us.
 */
@Dao
interface BookingDao {

    // ----------------------------------------------------
    // BOOKING QUERIES
    // ----------------------------------------------------

    // @Query executes a raw SQL command.
    // 'Flow' means it returns a reactive stream. If the database changes, the UI updates automatically!
    // We order by 'eventDate' descending so the newest events show up first.
    @Query("SELECT * FROM bookings ORDER BY eventDate DESC")
    fun getBookings(): Flow<List<BookingEntity>>

    // @Insert adds data to the table.
    // OnConflictStrategy.REPLACE means if an item with the same Primary Key exists, it overwrites it.
    // 'suspend' means this must run in the background (using Coroutines) so it doesn't freeze the UI.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<BookingEntity>)

    // Deletes all bookings from the local database.
    @Query("DELETE FROM bookings")
    suspend fun clearBookings()

    // ----------------------------------------------------
    // MENU QUERIES
    // ----------------------------------------------------

    // Gets all menu items from the local database. Returns a reactive Flow.
    @Query("SELECT * FROM menu_items")
    fun getMenuItems(): Flow<List<MenuItemEntity>>

    // Inserts or updates a list of menu items.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItems(items: List<MenuItemEntity>)

    // ----------------------------------------------------
    // ORDER QUERIES
    // ----------------------------------------------------

    // Gets all orders, ordered by when they were created (newest first).
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getOrders(): Flow<List<OrderEntity>>

    // Inserts a new order into the database.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

}
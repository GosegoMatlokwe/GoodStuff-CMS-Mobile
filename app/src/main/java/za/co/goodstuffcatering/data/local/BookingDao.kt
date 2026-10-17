package za.co.goodstuffcatering.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY eventDate DESC")
    fun getBookings(): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<BookingEntity>)

    @Query("DELETE FROM bookings")
    suspend fun clearBookings()

    // Menu Queries
    @Query("SELECT * FROM menu_items")
    fun getMenuItems(): Flow<List<MenuItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItems(items: List<MenuItemEntity>)

    // Order Queries
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

}
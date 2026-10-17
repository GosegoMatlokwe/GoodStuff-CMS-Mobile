package za.co.goodstuffcatering.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import za.co.goodstuffcatering.data.local.BookingDao
import za.co.goodstuffcatering.data.local.BookingEntity
import za.co.goodstuffcatering.data.remote.ApiService
import za.co.goodstuffcatering.data.remote.dto.BookingRequest
import za.co.goodstuffcatering.data.remote.dto.BookingResponse
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val apiService: ApiService,
    private val bookingDao: BookingDao
) {
    // Expose local DB data as a Flow
    val cachedBookings: Flow<List<BookingEntity>> = bookingDao.getBookings()

    suspend fun syncBookingsFromNetwork(token: String): Result<Unit> {
        return try {
            val response = apiService.getBookings("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                // Convert DTO to Entity and save to Room
                val entities = response.body()!!.map {
                    BookingEntity(
                        id = it.id,
                        eventDate = it.eventDate,
                        eventType = it.eventType,
                        status = it.status
                    )
                }
                bookingDao.insertBookings(entities)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to load bookings from server"))
            }
        } catch (e: Exception) {
            // If network fails, we return failure, but the UI still reads from Room!
            Result.failure(e)
        }
    }

    suspend fun createBooking(token: String, request: BookingRequest): Result<BookingResponse> {
        return try {
            val response = apiService.createBooking("Bearer $token", request)
            if (response.isSuccessful) {
                // Optionally trigger a sync here to update Room
                syncBookingsFromNetwork(token)
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Booking failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
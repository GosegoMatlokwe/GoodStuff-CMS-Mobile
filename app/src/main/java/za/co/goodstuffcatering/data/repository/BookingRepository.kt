package za.co.goodstuffcatering.data.repository

import kotlinx.coroutines.flow.Flow
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
    val cachedBookings: Flow<List<BookingEntity>> = bookingDao.getBookings()

    // FIXED: Removed 'token: String' from the parameters
    suspend fun syncBookingsFromNetwork(): Result<Unit> {
        return try {
            // FIXED: Removed "Bearer $token" from the API call
            val response = apiService.getBookings()

            if (response.isSuccessful && response.body() != null) {
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
            Result.failure(e)
        }
    }

    // FIXED: Removed 'token: String' from the parameters
    suspend fun createBooking(request: BookingRequest): Result<BookingResponse> {
        return try {
            // FIXED: Removed "Bearer $token" from the API call
            val response = apiService.createBooking(request)

            if (response.isSuccessful) {
                // Sync to update the local Room database
                syncBookingsFromNetwork()
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Booking failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

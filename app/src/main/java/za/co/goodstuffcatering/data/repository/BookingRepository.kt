package za.co.goodstuffcatering.data.repository

import za.co.goodstuffcatering.data.remote.ApiService
import za.co.goodstuffcatering.data.remote.dto.BookingRequest
import za.co.goodstuffcatering.data.remote.dto.BookingResponse
import za.co.goodstuffcatering.data.remote.dto.MenuItemDto
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getBookings(token: String): Result<List<BookingResponse>> {
        return try {
            val response = apiService.getBookings("Bearer $token")
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Failed to load bookings"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createBooking(token: String, request: BookingRequest): Result<BookingResponse> {
        return try {
            val response = apiService.createBooking("Bearer $token", request)
            if (response.isSuccessful) Result.success(response.body()!!)
            else Result.failure(Exception("Booking failed: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMenuItems(token: String): Result<List<MenuItemDto>> {
        return try {
            val response = apiService.getMenuItems("Bearer $token")
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Failed to load menu"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
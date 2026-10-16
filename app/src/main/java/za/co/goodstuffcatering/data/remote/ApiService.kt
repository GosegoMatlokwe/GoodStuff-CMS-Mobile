package za.co.goodstuffcatering.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import za.co.goodstuffcatering.data.remote.dto.AuthRequest
import za.co.goodstuffcatering.data.remote.dto.BookingRequest

interface ApiService {

    // Sync Firebase user with Django backend
    @POST("auth/sync/")
    suspend fun syncUser(
        @Header("Authorization") token: String,
        @Body request: AuthRequest
    ): Response<Unit>

    // Create a new booking
    @POST("bookings/create/")
    suspend fun createBooking(
        @Header("Authorization") token: String,
        @Body booking: BookingRequest
    ): Response<Unit>
}
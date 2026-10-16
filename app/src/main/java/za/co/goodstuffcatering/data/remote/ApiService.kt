package za.co.goodstuffcatering.data.remote

import retrofit2.Response
import retrofit2.http.*
import za.co.goodstuffcatering.data.remote.dto.*

interface ApiService {

    @POST("auth/sync/")
    suspend fun syncUser(
        @Header("Authorization") token: String,
        @Body request: AuthRequest
    ): Response<Unit>

    @GET("bookings/")
    suspend fun getBookings(
        @Header("Authorization") token: String
    ): Response<List<BookingResponse>>

    @POST("bookings/create/")
    suspend fun createBooking(
        @Header("Authorization") token: String,
        @Body booking: BookingRequest
    ): Response<BookingResponse>

    @GET("menu/")
    suspend fun getMenuItems(
        @Header("Authorization") token: String
    ): Response<List<MenuItemDto>>

    @DELETE("auth/delete-account/")
    suspend fun deleteAccount(
        @Header("Authorization") token: String
    ): Response<Unit>
}
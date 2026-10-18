package za.co.goodstuffcatering.data.remote

import retrofit2.Response
import retrofit2.http.*
import za.co.goodstuffcatering.data.remote.dto.*

interface ApiService {

    // @Header("Authorization") is removed! Our AuthInterceptor handles it automatically.
    @POST("auth/sync/")
    suspend fun syncUser(@Body request: AuthRequest): Response<Unit>

    @POST("auth/register/")
    suspend fun registerUser(@Body request: RegisterRequest): Response<Unit>

    @GET("bookings/")
    suspend fun getBookings(): Response<List<BookingResponse>>

    @POST("bookings/create/")
    suspend fun createBooking(@Body booking: BookingRequest): Response<BookingResponse>

    @GET("menu/")
    suspend fun getMenuItems(): Response<List<MenuItemDto>>

    @DELETE("auth/delete-account/")
    suspend fun deleteAccount(): Response<Unit>

    @POST("auth/consent/")
    suspend fun recordConsent(@Body request: ConsentRequest): Response<Unit>

    // NEW: Endpoint to delete a specific booking
    @DELETE("bookings/{id}/delete/")
    suspend fun deleteBooking(@Path("id") bookingId: Int): Response<Unit>
}
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

    @POST("auth/register/")
    suspend fun registerUser(
        @Header("Authorization") token: String,
        @Body request: RegisterRequest
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

    @POST("auth/consent/")
    suspend fun recordConsent(
        @Header("Authorization") token: String,
        @Body request: ConsentRequest
    ): Response<Unit>

    // Menu Discovery
    @GET("menu/categories/")
    suspend fun getMenuByCategory(@Query("category") category: String): Response<List<MenuItemDto>>

    // Order Management (Detailed)
    @POST("orders/create/")
    suspend fun createOrder(@Body request: OrderRequest): Response<OrderResponse>

    @GET("orders/{id}/status/")
    suspend fun getOrderStatus(@Path("id") orderId: String): Response<OrderStatusResponse>

    // Geolocation - Get Distance from Backend
    @POST("logistics/distance/")
    suspend fun calculateDistance(@Body request: DistanceRequest): Response<DistanceResponse>
}

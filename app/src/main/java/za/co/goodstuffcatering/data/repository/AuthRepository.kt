package za.co.goodstuffcatering.data.repository

import za.co.goodstuffcatering.data.remote.ApiService
import za.co.goodstuffcatering.data.remote.dto.AuthRequest
import za.co.goodstuffcatering.data.remote.dto.RegisterRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {
    // Notice: No 'token' parameter! The interceptor adds it.
    suspend fun syncUser(email: String, name: String): Result<Unit> {
        return try {
            val response = apiService.syncUser(AuthRequest(email, name))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Sync failed: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e) // Catch network crashes (e.g., if Wi-Fi is off)
        }
    }

    suspend fun registerUser(
        email: String,
        name: String,
        phone: String,
        popiaConsent: Boolean
    ): Result<Unit> {
        return try {
            if (!popiaConsent) {
                return Result.failure(Exception("POPIA consent is required."))
            }
            val request = RegisterRequest(
                email = email,
                name = name,
                phoneNumber = phone,
                popiaConsent = popiaConsent
            )
            val response = apiService.registerUser(request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Registration failed: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAccount(): Result<Unit> {
        return try {
            val response = apiService.deleteAccount()
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Delete failed: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

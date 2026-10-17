package za.co.goodstuffcatering.data.repository

import za.co.goodstuffcatering.data.remote.ApiService
import za.co.goodstuffcatering.data.remote.dto.AuthRequest
import za.co.goodstuffcatering.data.remote.dto.ConsentRequest
import za.co.goodstuffcatering.data.remote.dto.RegisterRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun syncUser(token: String, email: String, name: String): Result<Unit> {
        return try {
            val response = apiService.syncUser("Bearer $token", AuthRequest(email, name))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Sync failed: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun recordConsent(token: String, email: String): Result<Unit> {
        return try {
            val request = ConsentRequest(
                email = email,
                consentGiven = true,
                timestamp = System.currentTimeMillis()
            )
            val response = apiService.recordConsent("Bearer $token", request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Failed to record consent"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerUser(
        token: String,
        email: String,
        name: String,
        phone: String,
        popiaConsent: Boolean
    ): Result<Unit> {
        return try {
            if (!popiaConsent) {
                return Result.failure(Exception("POPIA consent is required to register."))
            }
            val request = RegisterRequest(
                email = email,
                name = name,
                phoneNumber = phone,
                popiaConsent = popiaConsent
            )
            val response = apiService.registerUser("Bearer $token", request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Registration failed: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAccount(token: String): Result<Unit> {
        return try {
            val response = apiService.deleteAccount("Bearer $token")
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Delete failed: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

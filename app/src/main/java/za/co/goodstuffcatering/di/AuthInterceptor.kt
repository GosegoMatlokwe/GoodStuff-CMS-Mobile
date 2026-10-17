package za.co.goodstuffcatering.di

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Only add token if the user is logged in
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // runBlocking is acceptable here because OkHttp interceptors run on background threads
            val token = runBlocking { user.getIdToken(false).await().token }

            if (token != null) {
                val newRequest = request.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                return chain.proceed(newRequest)
            }
        }
        return chain.proceed(request)
    }
}
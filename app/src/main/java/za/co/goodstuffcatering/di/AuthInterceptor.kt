package za.co.goodstuffcatering.di

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    // 'chain' contains the original request and allows us to proceed with it.
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Get the currently logged-in Firebase user
        val user = FirebaseAuth.getInstance().currentUser

        // Only add the token if the user is actually logged in
        if (user != null) {
            // runBlocking is usually bad, but OkHttp interceptors run on background threads,
            // so it's safe here to wait for the token to be fetched.
            // getIdToken(false) means: "Get the cached token. If it's expired, refresh it."
            val token = runBlocking { user.getIdToken(false).await().token }

            if (token != null) {
                // Build a new request and add the Authorization header
                val newRequest = request.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()

                // Proceed with the modified request
                return chain.proceed(newRequest)
            }
        }

        // If no user is logged in, proceed with the original request (Django will reject it with a 401)
        return chain.proceed(request)
    }
}
package za.co.goodstuffcatering.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import za.co.goodstuffcatering.data.remote.ApiService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        // This interceptor logs all API requests and responses in the Android Studio Logcat.
        // VERY useful for debugging why a request failed.
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            // Add our custom interceptor that attaches the Firebase JWT token
            .addInterceptor(authInterceptor)
            // Add the logging interceptor
            .addInterceptor(logging)
            // If the server takes longer than 30 seconds to connect or respond, fail the request.
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            // 🚨 CRITICAL: This is the IP address of your laptop running Django.
            // If you are using a physical phone, this must be your laptop's IPv4 address.
            // If you are using an Emulator, use "http://10.0.2.2:8000/api/"
            .baseUrl("http://192.168.1.36:8000/api/")
            .client(okHttpClient)
            // GsonConverterFactory translates JSON from Django into Kotlin objects (and vice versa)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        // Tells Retrofit to create the implementation for our ApiService interface
        return retrofit.create(ApiService::class.java)
    }
}

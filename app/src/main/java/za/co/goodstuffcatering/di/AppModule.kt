package za.co.goodstuffcatering.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import za.co.goodstuffcatering.data.local.AppDatabase
import za.co.goodstuffcatering.data.local.BookingDao
import javax.inject.Singleton

/**
 * @Module tells Hilt this class contains instructions on how to build things.
 * @InstallIn(SingletonComponent::class) means these dependencies live as long as the app is running.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * @Provides tells Hilt: "When someone asks for an AppDatabase, run this function to build it."
     * @Singleton means only ONE instance of the database will ever be created (saving memory).
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "goodstuff_db" // The actual file name of the database on the phone
        )
            // fallbackToDestructiveMigration means if you change the database version (e.g., add a column),
            // it will just delete the old data and recreate the tables. Great for development, bad for production.
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * Hilt provides the AppDatabase, then we extract the BookingDao from it.
     */
    @Provides
    fun provideBookingDao(database: AppDatabase): BookingDao {
        return database.bookingDao()
    }
}
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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "goodstuff_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideBookingDao(database: AppDatabase): BookingDao {
        return database.bookingDao()
    }
}
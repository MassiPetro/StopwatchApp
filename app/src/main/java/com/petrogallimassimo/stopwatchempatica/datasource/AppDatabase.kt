package com.petrogallimassimo.stopwatchempatica.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerStatisticsModel

/**
 * The Room database
 */
@Database(entities = [FootballPlayerStatisticsModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun footballPlayerStatisticsDao(): FootBallPlayerStatisticsDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                instance = inst
                inst
            }
        }

    }
}
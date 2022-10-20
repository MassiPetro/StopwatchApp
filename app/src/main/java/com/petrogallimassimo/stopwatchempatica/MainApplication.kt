package com.petrogallimassimo.stopwatchempatica

import android.app.Application
import com.petrogallimassimo.stopwatchempatica.datasource.AppDatabase
import com.petrogallimassimo.stopwatchempatica.datasource.FootballPlayerStatisticsRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class MainApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy {
        AppDatabase.getDatabase(this)
    }
    lateinit var footballPlayerStatisticsRepository: FootballPlayerStatisticsRepository

    // DI of football players statistics DB repository
    init {
        applicationScope.launch {
            footballPlayerStatisticsRepository =
                FootballPlayerStatisticsRepository(database.footballPlayerStatisticsDao())
        }
    }
}
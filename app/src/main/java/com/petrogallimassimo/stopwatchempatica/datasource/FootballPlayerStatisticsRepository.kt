package com.petrogallimassimo.stopwatchempatica.datasource

import androidx.annotation.WorkerThread
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerStatisticsModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FootballPlayerStatisticsRepository @Inject constructor(private val footBallPlayerStatisticsDao: FootBallPlayerStatisticsDao) {

    @WorkerThread
    fun insert(footballPlayerStatisticsModel: FootballPlayerStatisticsModel) {
        footBallPlayerStatisticsDao.insertAll(footballPlayerStatisticsModel)
    }

    @WorkerThread
    fun getAll() = footBallPlayerStatisticsDao.getAll()
}
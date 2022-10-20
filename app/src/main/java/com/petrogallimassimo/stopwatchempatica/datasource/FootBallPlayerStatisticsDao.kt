package com.petrogallimassimo.stopwatchempatica.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerStatisticsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FootBallPlayerStatisticsDao {
    @Query("SELECT * FROM FootballPlayerEntity")
    fun getAll(): Flow<List<FootballPlayerStatisticsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg footballPlayerStatisticsModel: FootballPlayerStatisticsModel)

    @Delete
    fun delete(footballPlayerStatisticsModel: FootballPlayerStatisticsModel)
}
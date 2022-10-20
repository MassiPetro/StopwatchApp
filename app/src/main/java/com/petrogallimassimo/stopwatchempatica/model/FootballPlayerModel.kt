package com.petrogallimassimo.stopwatchempatica.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FootballPlayerEntity")
data class FootballPlayerStatisticsModel(
    @Embedded var footballPlayer: FootballPlayerUiModel,
    @Embedded var metrics: TrainingMetricsModel
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}

data class FootballPlayerUiModel(
    val title: String? = null,
    val first: String? = null,
    val last: String? = null,
    val large: String? = null,
    val medium: String? = null,
    val thumbnail: String? = null
) {
    override fun toString(): String {
        return "FootballPlayer Informations: (title=$title, first=$first, last=$last, large=$large, medium=$medium, thumbnail=$thumbnail)"
    }
}

data class TrainingMetricsModel(
    val peakSpeed: String? = null,
    val lapsNumber: String? = null
) {
    override fun toString(): String {
        return "FootballPlayer Training Metrics: (peakSpeed=$peakSpeed, lapsNumber=$lapsNumber)"
    }
}
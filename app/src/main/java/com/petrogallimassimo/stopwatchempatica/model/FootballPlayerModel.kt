package com.petrogallimassimo.stopwatchempatica.model

data class FootballPlayerStatisticsModel(
    val id: String,
    var footballPlayer: FootballPlayerUiModel,
    var metrics: TrainingMetricsModel
)

data class FootballPlayerUiModel(
    val title: String? = null,
    val first: String? = null,
    val last: String? = null,
    val large: String? = null,
    val medium: String? = null,
    val thumbnail: String? = null
)

data class TrainingMetricsModel(
    val peakSpeed: String? = null,
    val lapsNumber: String? = null
)
package com.petrogallimassimo.stopwatchempatica.datasource

import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerUiModel

data class FootballPlayerResponse(
    val results: List<FootballPlayerResponseObject>? = null
) {
    data class FootballPlayerResponseObject(
        val name: NamePlayer? = null,
        val picture: PicturePlayer? = null
    ) {
        data class NamePlayer(
            val title: String? = null,
            val first: String? = null,
            val last: String? = null
        )

        data class PicturePlayer(
            val large: String? = null,
            val medium: String? = null,
            val thumbnail: String? = null
        )
    }

    fun mapToFootballPlayerUiModel() = results?.map {
        FootballPlayerUiModel(
            title = it.name?.title ?: "",
            first = it.name?.first ?: "",
            last = it.name?.last ?: "",
            large = it.picture?.large ?: "",
            medium = it.picture?.medium ?: "",
            thumbnail = it.picture?.thumbnail ?: "",
        )
    }
}
package com.petrogallimassimo.stopwatchempatica.datasource

import kotlinx.coroutines.flow.flow

class Repository(
    private val apiService: ApiService
) {

    suspend fun getFootballPlayers() = flow {
        val listPlayers = apiService.getFootballPlayers()

        emit(listPlayers)
    }

}
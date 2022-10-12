package com.petrogallimassimo.stopwatchempatica.datasource

import retrofit2.http.GET

interface ApiService {

    @GET("?seed=empatica&inc=name,picture&gender=male&results=10&noinfo")
    suspend fun getFootballPlayers(): FootballPlayerResponse
}
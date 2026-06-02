package com.example.cineversemovieapp.repository

import com.example.cineversemovieapp.data.remote.dto.MovieDNA
import com.example.cineversemovieapp.network.Auth.AuthApi

class SceneDNARepository(
    private val api: AuthApi
) {
    suspend fun getSceneDNA(
        movieId: Long
    ): MovieDNA {
        return api.getSceneDNA(movieId)
    }
}

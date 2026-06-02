package com.example.cineversemovieapp.repository

import com.example.cineversemovieapp.models.AddWatchlistRequest
import com.example.cineversemovieapp.models.LoginRequest
import com.example.cineversemovieapp.models.RegisterRequest
import com.example.cineversemovieapp.models.UpdateUserRequest
import com.example.cineversemovieapp.network.RetrofitInstance

class AuthRepository {
    private val api = RetrofitInstance.api

    suspend fun registerUser(request: RegisterRequest) = api.register(request)

    suspend fun login(request: LoginRequest) = api.login(request)

    suspend fun updateUser(id: Long, request: UpdateUserRequest) =
        api.updateUser(id, request)
    suspend fun getUserById(id: Long) =
        api.getUserById(id)

    suspend fun getWatchlist(userId: Long) = api.getWatchlist(userId)

    suspend fun addToWatchlist(userId: Long, request: AddWatchlistRequest) =
        api.addToWatchlist(userId, request)

    suspend fun removeFromWatchlist(userId: Long, tmdbId: Long) =
        api.removeFromWatchlist(userId, tmdbId)

    suspend fun checkWatchlist(userId: Long, tmdbId: Long) =
        api.checkWatchlist(userId, tmdbId)

    suspend fun getMovies() = api.getMovies()

    suspend fun getFeaturedMovies() = api.getFeaturedMovies()
}

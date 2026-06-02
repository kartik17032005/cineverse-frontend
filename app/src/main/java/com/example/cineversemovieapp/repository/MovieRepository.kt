package com.example.cineversemovieapp.repository

import com.example.cineversemovieapp.network.RetrofitInstance

class MovieRepository {
    private val api = RetrofitInstance.api

    suspend fun getAllMovies() = api.getMovies()
    suspend fun getFeaturedMovies() = api.getFeaturedMovies()
    suspend fun getMoviesByGenre(genre: String) = api.getMoviesByGenre(genre)
    suspend fun getMovieById(id: Long) = api.getMovieById(id)

}
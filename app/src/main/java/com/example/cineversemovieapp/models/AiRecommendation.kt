package com.example.cineversemovieapp.models

data class AiRecommendation(val query: String)

data class AiMovieResult(
    val tmdbId: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: Double,
    val releaseYear: String,
    val aiReason: String    // why groq recommended this
)

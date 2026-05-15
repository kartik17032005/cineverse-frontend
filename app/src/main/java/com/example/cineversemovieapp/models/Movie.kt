package com.example.cineversemovieapp.models

data class Movie (
    val movieId: Long,
    val title: String,
    val description: String,
    val posterUrl: String,
    val releaseYear: Int,
    val rating: Double,
    val genre: String,
    val duration: String,
    val trailerKey: String,
    val isFeatured: Boolean,
    val language: String,
    val tmdbId: Long
)
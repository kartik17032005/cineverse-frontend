package com.example.cineversemovieapp.models

data class WatchlistItem(
    val id: Long,
    val tmdbId: Long,
    val movieTitle: String,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: String,
    val releaseYear: String,
    val genre: String,
    val addedAt: String
)

data class AddWatchlistRequest(
    val tmdbId: Long,
    val movieTitle: String?,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: String,
    val releaseYear: String,
    val genre: String
)
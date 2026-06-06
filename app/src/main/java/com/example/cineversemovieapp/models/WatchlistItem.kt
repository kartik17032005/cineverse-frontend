package com.example.cineversemovieapp.models

import com.google.gson.annotations.SerializedName

data class WatchlistItem(
    val id: Long,
    val tmdbId: Long,
    val movieTitle: String,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: String,
    val releaseYear: String,
    val genre: String,
    val addedAt: String,
    @SerializedName("isTv")
    val isTv: Boolean = false
)

data class AddWatchlistRequest(
    val tmdbId: Long,
    val movieTitle: String?,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: String,
    val releaseYear: String,
    val genre: String,
    val isTv: Boolean = false
)

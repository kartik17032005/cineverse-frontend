package com.example.cineversemovieapp.data

data class ReelVideo(
    val movieId: Int,
    val title: String,
    val backdropUrl: String,
    val posterUrl: String,
    val rating: String,
    val genre: String,
    val overview: String,
    val year: String,
    val videoUrl: String?,   // 🔥 NEW
    val trailerKey: String?  // 🔥 NEW
)
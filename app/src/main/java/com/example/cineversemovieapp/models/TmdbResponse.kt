package com.example.cineversemovieapp.models

import com.example.cineversemovieapp.data.tmdb.TmdbMovie

data class TmdbResponse (
    val page: Int,
    val results: List<TmdbMovie>
)
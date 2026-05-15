package com.example.cineversemovieapp.models

data class TmdbVideosResponse(
    val results: List<TmdbVideo>
)

data class TmdbVideo(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String,
    val official: Boolean
){
    val youtubeUrl: String
        get() = "https://www.youtube.com/watch?v=$key"

    val youtubeThumbnail: String
        get() = "https://img.youtube.com/vi/$key/hqdefault.jpg"
}
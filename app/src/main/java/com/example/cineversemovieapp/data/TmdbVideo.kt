package com.example.cineversemovieapp.data

data class TmdbVideosResponse(
    val results: List<TmdbVideo>
)

data class TmdbVideo(
    val id: String,
    val key: String,        // YouTube video ID e.g. "5KbhbXz2J0E"
    val name: String,
    val site: String,       // "YouTube" or "Vimeo"
    val type: String,       // "Trailer" "Teaser" "Clip" "Behind the Scenes"
    val official: Boolean
) {
    // full YouTube watch URL
    val youtubeUrl: String
        get() = "https://www.youtube.com/watch?v=$key"

    // thumbnail of the trailer
    val youtubeThumbnail: String
        get() = "https://img.youtube.com/vi/$key/hqdefault.jpg"
}
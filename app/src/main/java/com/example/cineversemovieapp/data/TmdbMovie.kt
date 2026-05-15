package com.example.cineversemovieapp.data

import com.example.cineversemovieapp.utils.Constants
import com.google.gson.annotations.SerializedName

data class TmdbMovie(
    val id: Int,
    val title: String?,
    val name: String?,
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("genre_ids")
    val genreIds: List<Int>
) {
    val displayTitle: String
        get() = title ?: name ?: "Unknown"

    val posterUrl: String
        get() = "${Constants.TMDB_IMAGE_BASE_URL}$posterPath"

    val backdropUrl: String
        get() = "${Constants.TMDB_BACKDROP_BASE_URL}$backdropPath"

    val releaseYear: String
        get() = releaseDate?.take(4) ?: ""

    val rating: String
        get() = "%.1f".format(voteAverage)

    fun getGenreName(): String {
        return when (genreIds.firstOrNull()) {
            28 -> "ACTION"
            12 -> "ADVENTURE"
            16 -> "ANIMATION"
            35 -> "COMEDY"
            80 -> "CRIME"
            99 -> "DOCUMENTARY"
            18 -> "DRAMA"
            10751 -> "FAMILY"
            14 -> "FANTASY"
            36 -> "HISTORY"
            27 -> "HORROR"
            10402 -> "MUSIC"
            9648 -> "MYSTERY"
            10749 -> "ROMANCE"
            878 -> "SCI-FI"
            10770 -> "TV MOVIE"
            53 -> "THRILLER"
            10752 -> "WAR"
            37 -> "WESTERN"
            else -> "MOVIE"
        }
    }
}
// add at bottom of TmdbMovie.kt
fun TmdbMovie.toReelItem(): ReelVideo {
    return ReelVideo(
        movieId = id,
        title = displayTitle,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl,
        rating = rating,
        genre = getGenreName(), // ✅ FIXED
        overview = overview,
        year = releaseYear,
        videoUrl = null,       // 🔥 for ExoPlayer (we'll map later)
        trailerKey = null      // 🔥 for YouTube fallback
    )
}


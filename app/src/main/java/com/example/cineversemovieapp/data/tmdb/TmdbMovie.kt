package com.example.cineversemovieapp.data.tmdb

import com.example.cineversemovieapp.data.ReelVideo
import com.example.cineversemovieapp.utils.Constants
import com.google.gson.annotations.SerializedName

data class TmdbMovie(
    val id: Int,
    val title: String?,
    val name: String?,
    val overview: String?,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("first_air_date")
    val firstAirDate: String?,

    @SerializedName("vote_average")
    val voteAverage: Double?,

    @SerializedName("genre_ids")
    val genreIds: List<Int>?,

    @SerializedName("media_type")
    val mediaType: String?
) {
    val displayTitle: String
        get() = title ?: name ?: "Unknown"

    val posterUrl: String
        get() = if (posterPath != null) "${Constants.TMDB_IMAGE_BASE_URL}$posterPath" else ""

    val backdropUrl: String
        get() = if (backdropPath != null) "${Constants.TMDB_BACKDROP_BASE_URL}$backdropPath" else ""

    val releaseYear: String
        get() = (releaseDate ?: firstAirDate)?.take(4) ?: ""

    val rating: String
        get() = "%.1f".format(voteAverage ?: 0.0)

    fun getGenreName(): String {
        return when (genreIds?.firstOrNull()) {
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

fun TmdbMovie.toReelItem(): ReelVideo {
    return ReelVideo(
        movieId = id,
        title = displayTitle,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl,
        rating = rating,
        genre = getGenreName(),
        overview = overview ?: "",
        year = releaseYear,
        videoUrl = null,
        trailerKey = null
    )
}

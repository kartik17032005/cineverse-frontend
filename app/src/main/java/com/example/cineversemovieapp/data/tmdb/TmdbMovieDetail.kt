package com.example.cineversemovieapp.data.tmdb

import com.example.cineversemovieapp.utils.Constants
import com.google.gson.annotations.SerializedName

data class TmdbMovieDetail(
    val id: Int,
    val title: String?,
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int,

    val runtime: Int?,
    val genres: List<TmdbGenre>,

    @SerializedName("spoken_languages")
    val spokenLanguages: List<TmdbLanguage>

) {
    val posterUrl: String
        get() = "${Constants.TMDB_IMAGE_BASE_URL}$posterPath"

    val backdropUrl: String
        get() = "${Constants.TMDB_BACKDROP_BASE_URL}$backdropPath"

    val releaseYear: String
        get() = releaseDate?.take(4) ?: ""

    val rating: String
        get() = "%.1f".format(voteAverage)

    val duration: String
        get() = if (runtime != null && runtime > 0) {
            val hours = runtime / 60
            val minutes = runtime % 60
            "${hours}h ${minutes}m"
        } else "N/A"

    val voteCountFormatted: String
        get() = when {
            voteCount >= 1_000_000 -> "%.1fM".format(voteCount / 1_000_000.0)
            voteCount >= 1_000 -> "%.1fK".format(voteCount / 1_000.0)
            else -> voteCount.toString()
        }

    val language: String
        get() = spokenLanguages.firstOrNull()?.englishName ?: "English"
}

data class TmdbGenre(
    val id: Int,
    val name: String
)

data class TmdbLanguage(
    val id: Int? = null,

    @SerializedName("english_name")
    val englishName: String
)
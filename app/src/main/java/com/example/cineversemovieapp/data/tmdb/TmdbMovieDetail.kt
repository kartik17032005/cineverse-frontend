package com.example.cineversemovieapp.data.tmdb

import com.example.cineversemovieapp.utils.Constants
import com.google.gson.annotations.SerializedName

data class TmdbMovieDetail(
    val id: Int,
    val title: String?, // For Movies
    val name: String?,  // For TV Shows
    val overview: String?,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("release_date")
    val releaseDate: String?, // For Movies

    @SerializedName("first_air_date")
    val firstAirDate: String?, // For TV Shows

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int,

    val runtime: Int?, // For Movies

    @SerializedName("episode_run_time")
    val episodeRunTime: List<Int>?, // For TV Shows

    val genres: List<TmdbGenre>?,

    @SerializedName("spoken_languages")
    val spokenLanguages: List<TmdbLanguage>?

) {
    val displayTitle: String
        get() = title ?: name ?: "Unknown"

    val posterUrl: String
        get() = "${Constants.TMDB_IMAGE_BASE_URL}$posterPath"

    val backdropUrl: String
        get() = "${Constants.TMDB_BACKDROP_BASE_URL}$backdropPath"

    val releaseYear: String
        get() = (releaseDate ?: firstAirDate)?.take(4) ?: "N/A"

    val rating: String
        get() = "%.1f".format(voteAverage)

    val duration: String
        get() {
            val mins = runtime ?: episodeRunTime?.firstOrNull() ?: 0
            return if (mins > 0) {
                val hours = mins / 60
                val minutes = mins % 60
                if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
            } else "N/A"
        }

    val voteCountFormatted: String
        get() = when {
            voteCount >= 1_000_000 -> "%.1fM".format(voteCount / 1_000_000.0)
            voteCount >= 1_000 -> "%.1fK".format(voteCount / 1_000.0)
            else -> voteCount.toString()
        }

    val language: String
        get() = spokenLanguages?.firstOrNull()?.englishName ?: "English"
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

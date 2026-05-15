package com.example.cineversemovieapp.data

import com.example.cineversemovieapp.utils.Constants
import com.google.gson.annotations.SerializedName

data class TmdbTvResponse(
    val page: Int,
    val results: List<TmdbTvShow>
)

data class TmdbTvShow(
    val id: Int,
    val name: String?,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("first_air_date")
    val firstAirDate: String?,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("genre_ids")
    val genreIds: List<Int>,

    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int? = null,

    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int? = null,

    @SerializedName("origin_country")
    val originCountry: List<String> = emptyList()

) {
    val posterUrl: String
        get() = "${Constants.TMDB_IMAGE_BASE_URL}$posterPath"

    val backdropUrl: String
        get() = "${Constants.TMDB_BACKDROP_BASE_URL}$backdropPath"

    val releaseYear: String
        get() = firstAirDate?.take(4) ?: ""

    val rating: String
        get() = "%.1f".format(voteAverage)

    val episodeCount: String
        get() = if (numberOfEpisodes != null && numberOfEpisodes > 0)
            "$numberOfEpisodes eps"
        else
            "Ongoing"

    fun getAnimGenreTag(): String {
        return when (genreIds.firstOrNull()) {
            10759 -> "ACTION"
            16 -> "ANIME"
            35 -> "COMEDY"
            18 -> "DRAMA"
            10765 -> "SCI-FI"
            9648 -> "MYSTERY"
            10762 -> "KIDS"
            else -> "ANIME"
        }
    }
}
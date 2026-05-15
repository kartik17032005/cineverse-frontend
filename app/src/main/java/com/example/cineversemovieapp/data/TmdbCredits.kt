package com.example.cineversemovieapp.data

import com.google.gson.annotations.SerializedName

data class TmdbCreditsResponse(
    val id: Int,
    val cast: List<TmdbCastMember>
)

data class TmdbCastMember(
    val id: Int,
    val name: String,
    val character: String,
    val order: Int,

    @SerializedName("profile_path")
    val profilePath: String?
) {
    val profileUrl: String
        get() = if (profilePath != null)
            "https://image.tmdb.org/t/p/w185$profilePath"
        else
            "https://i.pravatar.cc/64?img=${id % 70}"
}
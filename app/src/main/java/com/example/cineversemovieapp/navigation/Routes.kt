package com.example.cineversemovieapp.navigation

import android.net.Uri

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Login: Routes("login")
    object Register: Routes("register")
    object Home: Routes("home")
    
    object MovieDetail : Routes("movie_detail/{movieId}/{isTv}") {
        fun createRoute(movieId: Int, isTv: Boolean = false) = "movie_detail/$movieId/$isTv"
    }

    object Profile: Routes("profile")
    object AiRecommend: Routes("ai_recommend")
    object Search: Routes("search")
    object EditProfile: Routes("edit_profile")
    object Mood : Routes("mood")
    object Reels : Routes("reels")
    object Watchlist : Routes("watchlist")
    object Badges : Routes("badges")
    
    // Player Route
    object MoviePlayer : Routes("movie_player/{movieTitle}/{movieYear}/{videoUrl}") {
        fun createRoute(title: String, year: String, url: String): String {
            return "movie_player/${Uri.encode(title)}/${Uri.encode(year)}/${Uri.encode(url)}"
        }
    }

    object SceneDNA : Routes("scene_dna/{movieId}") {
        fun createRoute(movieId: Int) = "scene_dna/$movieId"
    }

    object CompareDNA : Routes("compare_dna/{movieId}") {
        fun createRoute(movieId: Int) = "compare_dna/$movieId"
    }

    object RealCompareDNA: Routes("real_compare_dna/{baseMovieId}/{targetMovieId}"){
        fun createRoute(baseMovieId: Int, targetMovieId: Int) = "real_compare_dna/$baseMovieId/$targetMovieId"
    }
}

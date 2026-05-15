package com.example.cineversemovieapp.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash") // 👈 added splash route
    object Login: Routes("login")
    object Register: Routes("register")
    object Home: Routes("home")
    object MovieDetail : Routes("movie_detail/{movieId}") {
        fun createRoute(movieId: Int) = "movie_detail/$movieId"
    }
    object Profile: Routes("profile")

    object AiRecommend: Routes("ai_recommend")

    object Search: Routes("search")
    object EditProfile: Routes("edit_profile")
    object Mood : Routes("mood")
    object Reels : Routes("reels")
    object Watchlist : Routes("watchlist")
}
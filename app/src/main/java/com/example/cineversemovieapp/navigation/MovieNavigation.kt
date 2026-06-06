package com.example.cineversemovieapp.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cineversemovieapp.screens.*
import com.example.cineversemovieapp.screens.ai.AiRecommendScreen
import com.example.cineversemovieapp.screens.badge.CineBadgesScreen
import com.example.cineversemovieapp.screens.details.MovieDetailsScreen
import com.example.cineversemovieapp.screens.home.HomeScreen
import com.example.cineversemovieapp.screens.auth.LoginScreen
import com.example.cineversemovieapp.screens.auth.RegisterScreen
import com.example.cineversemovieapp.screens.dna.SceneDNAScreen
import com.example.cineversemovieapp.screens.dna.compareDNA.CompareDNAScreen
import com.example.cineversemovieapp.screens.dna.compareDNA.DNAComparisonScreen
import com.example.cineversemovieapp.screens.player.MoviePlayerScreen
import com.example.cineversemovieapp.screens.profile.EditProfileScreen
import com.example.cineversemovieapp.screens.profile.ProfileScreen
import com.example.cineversemovieapp.screens.search.SearchScreen
import com.example.cineversemovieapp.screens.splash.SplashScreen
import com.example.cineversemovieapp.screens.watchlist.WatchlistScreen
import com.example.cineversemovieapp.viewmodel.AuthViewModel
import com.example.cineversemovieapp.viewmodel.BadgeViewModel
import com.example.cineversemovieapp.viewmodel.TmdbViewModel
import com.example.cineversemovieapp.viewmodel.SceneDNAViewModel

private const val TRANSITION_DURATION = 400

@Composable
fun MovieNavigation() {
    val navController = rememberNavController()

    val tmdbViewModel: TmdbViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val badgeViewModel: BadgeViewModel = viewModel()
    val sceneDNAViewModel: SceneDNAViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(TRANSITION_DURATION)
            ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))
        }
    ) {

        // ── Splash Screen ──
        composable(
            route = Routes.Splash.route,
        ) {
            SplashScreen(navController = navController)
        }

        composable(Routes.Login.route) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Routes.Register.route) {
            RegisterScreen(navController = navController)
        }

        composable(Routes.Home.route) {
            HomeScreen(
                navController = navController,
                tmdbViewModel = tmdbViewModel,
                authViewModel = authViewModel
            )
        }

        composable(
            route = Routes.MovieDetail.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.StringType },
                navArgument("isTv") { type = NavType.BoolType; defaultValue = false }
            )
        ) { backStackEntry ->
            val movieIdStr = backStackEntry.arguments?.getString("movieId") ?: "0"
            val movieId = movieIdStr.toIntOrNull() ?: 0
            val isTv = backStackEntry.arguments?.getBoolean("isTv") ?: false
            MovieDetailsScreen(
                movieId = movieId,
                isTv = isTv,
                navController = navController,
                tmdbViewModel = tmdbViewModel,
                authViewModel = authViewModel
            )
        }

        composable(Routes.Search.route) {
            SearchScreen(navController = navController)
        }

        composable(Routes.Profile.route) {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel,
                badgeViewModel = badgeViewModel
            )
        }

        composable(Routes.EditProfile.route) {
            EditProfileScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Routes.Mood.route) {
            MoodScreen(
                navController = navController,
                tmdbViewModel = tmdbViewModel
            )
        }

        composable(Routes.Reels.route) {
            ReelsScreen(
                navController = navController,
                tmdbViewModel = tmdbViewModel
            )
        }

        composable(Routes.Watchlist.route) {
            WatchlistScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Routes.AiRecommend.route) {
            AiRecommendScreen(navController = navController)
        }

        composable(Routes.Badges.route) {
            CineBadgesScreen(
                onBack = { navController.popBackStack() },
                viewModel = badgeViewModel
            )
        }

        composable(Routes.MoviePlayer.route) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("movieTitle") ?: ""
            val year = backStackEntry.arguments?.getString("movieYear") ?: ""
            val url = backStackEntry.arguments?.getString("videoUrl") ?: ""
            MoviePlayerScreen(
                movieTitle = title,
                movieYear = year,
                videoUrl = url,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SceneDNA.route) { backStackEntry ->
            val movieIdStr = backStackEntry.arguments?.getString("movieId") ?: "0"
            val movieId = movieIdStr.toIntOrNull() ?: 0
            SceneDNAScreen(
                movieId = movieId,
                navController = navController,
                viewModel = sceneDNAViewModel,
                tmdbViewModel = tmdbViewModel
            )
        }

        composable(Routes.CompareDNA.route) { backStackEntry ->
            val movieIdStr = backStackEntry.arguments?.getString("movieId") ?: "0"
            val movieId = movieIdStr.toIntOrNull() ?: 0
            CompareDNAScreen(
                movieId = movieId,
                navController = navController,
                tmdbViewModel = tmdbViewModel
            )
        }

        composable(
            route = Routes.RealCompareDNA.route,
            arguments = listOf(
                navArgument("baseMovieId") { type = NavType.IntType },
                navArgument("targetMovieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val baseMovieId = backStackEntry.arguments?.getInt("baseMovieId") ?: 0
            val targetMovieId = backStackEntry.arguments?.getInt("targetMovieId") ?: 0
            DNAComparisonScreen(
                baseMovieId = baseMovieId,
                targetMovieId = targetMovieId,
                navController = navController,
                tmdbViewModel = tmdbViewModel,
                sceneDNAViewModel = sceneDNAViewModel
            )
        }
    }
}

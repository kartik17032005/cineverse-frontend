package com.example.cineversemovieapp.components.bannerItems

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cineversemovieapp.data.tmdb.TmdbMovie
import com.example.cineversemovieapp.models.Movie
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun FeaturedPager(
    movies: List<Any>, // Accepts both Movie and TmdbMovie
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    if (movies.isEmpty()) return

    val pagerState = rememberPagerState(
        pageCount = { movies.size }
    )

    // ── Auto scroll every 3 seconds ──
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            if (movies.isNotEmpty()) {
                val nextPage = (pagerState.settledPage + 1) % movies.size
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec = tween(durationMillis = 600)
                )
            }
        }
    }

    Column {
        // ── Horizontal Pager ──
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val movieItem = movies[page]
            
            // Map to common model or handle both types
            val displayMovie = when (movieItem) {
                is TmdbMovie -> Movie(
                    movieId = movieItem.id.toLong(),
                    title = movieItem.displayTitle,
                    description = movieItem.overview ?: "",
                    posterUrl = movieItem.posterUrl,
                    releaseYear = movieItem.releaseYear.toIntOrNull() ?: 0,
                    rating = movieItem.voteAverage ?: 0.0,
                    genre = movieItem.getGenreName(),
                    duration = "2h", // Default
                    trailerKey = "",
                    isFeatured = true,
                    language = "en",
                    tmdbId = movieItem.id.toLong()
                )
                is Movie -> movieItem
                else -> null
            }

            displayMovie?.let {
                HeroBanner(
                    movie = it,
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }

        // ── Dot Indicators ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            movies.forEachIndexed { index, _ ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .height(6.dp)
                        .width(if (isSelected) 20.dp else 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (isSelected) gold
                            else Color(0xFF5E5C68)
                        )
                )
            }
        }
    }
}

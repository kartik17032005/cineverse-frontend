package com.example.cineversemovieapp.components.bannerItems

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.models.Movie
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.AuthViewModel

@Composable
fun HeroBanner(
    movie: Movie,
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val gold = gold

    val watchlistIds by authViewModel.watchlistIds.collectAsState()
    val isBookmarked = watchlistIds.contains(movie.tmdbId)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable {
                if (movie.tmdbId != 0L) {
                    navController.navigate(
                        Routes.MovieDetail.createRoute(movie.tmdbId.toInt())
                    )
                }
            },
        contentAlignment = Alignment.BottomStart
    ) {

        // ── Poster Image ──
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.posterUrl)
                .crossfade(true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // ── Gradient Overlay ──
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.92f)
                        )
                    )
                )
        )

        // ── Content ──
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // ── Now Trending Badge ──
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(0x26CCA246))
                    .border(1.dp, Color(0x4DCCA246), RoundedCornerShape(100.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "● Now Trending",
                    color = gold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // ── Movie Title ──
            Text(
                text = movie.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                fontFamily = bebasNeue,
                letterSpacing = 4.sp
            )

            // ── Meta Info ──
            Text(
                text = "${movie.releaseYear}  ·  ${movie.duration}  ·  ${movie.genre}  ·  ★ ${movie.rating}",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )

            // ── Action Buttons ──
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Watch Now
                Button(
                    modifier = Modifier
                        .height(46.dp)
                        .width(140.dp),
                    onClick = {
                        if (movie.tmdbId != 0L) {
                            navController.navigate(
                                Routes.MovieDetail.createRoute(movie.tmdbId.toInt())
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = gold)
                ) {
                    Text(
                        text = "▶ Watch Now",
                        color = Color(0xFF101017),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                // Watchlist
                Button(
                    modifier = Modifier
                        .height(46.dp)
                        .width(120.dp),
                    onClick = {
                        if (movie.tmdbId != 0L) {
                            if (isBookmarked) {
                                authViewModel.removeFromWatchlist(movie.tmdbId)
                            } else {
                                authViewModel.addToWatchlist(
                                    tmdbId = movie.tmdbId,
                                    title = movie.title,
                                    posterUrl = movie.posterUrl,
                                    backdropUrl = movie.posterUrl,
                                    rating = movie.rating.toString(),
                                    releaseYear = movie.releaseYear.toString(),
                                    genre = movie.genre
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBookmarked) gold.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.4f)
                    ),
                    border = BorderStroke(1.dp, if (isBookmarked) gold.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.15f))
                ) {
                    Text(
                        text = if (isBookmarked) "✓ Added" else "+ Watchlist",
                        color = if (isBookmarked) gold else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
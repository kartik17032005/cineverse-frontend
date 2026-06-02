package com.example.cineversemovieapp.screens.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.models.WatchlistItem
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.AuthViewModel

@Composable
fun WatchlistScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val gold = gold
    val bg = Color(0xFF0A0A0F)
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    val watchlistState by authViewModel.watchlistState.collectAsState()

    // load watchlist when screen opens
    LaunchedEffect(Unit) {
        authViewModel.getWatchlist()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {

        // ── Header ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 18.dp,
                    end = 18.dp,
                    top = 52.dp,
                    bottom = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Back Button ──
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A1A24))
                    .border(1.dp, Color.White.copy(alpha = 0.07f), CircleShape)
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            // ── Title ──
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "MY",
                    fontFamily = bebasNeue,
                    fontSize = 28.sp,
                    letterSpacing = 3.sp,
                    color = Color.White,
                    lineHeight = 28.sp
                )
                Text(
                    text = "WATCHLIST",
                    fontFamily = bebasNeue,
                    fontSize = 28.sp,
                    letterSpacing = 3.sp,
                    color = gold,
                    lineHeight = 28.sp
                )
            }

            // ── Bookmark Icon ──
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(gold.copy(alpha = 0.1f))
                    .border(1.dp, gold.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = null,
                    tint = gold,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        when (watchlistState) {

            // ── Loading ──
            is AuthViewModel.WatchlistState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = gold)
                }
            }

            // ── Empty ──
            is AuthViewModel.WatchlistState.Success -> {
                val items = (watchlistState as AuthViewModel.WatchlistState.Success).items

                if (items.isEmpty()) {
                    // empty state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = "🎬", fontSize = 56.sp)

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "NO MOVIES YET",
                                fontFamily = bebasNeue,
                                fontSize = 22.sp,
                                letterSpacing = 3.sp,
                                color = Color(0xFF5E5C68)
                            )

                            Text(
                                text = "Bookmark movies to save them here",
                                fontSize = 13.sp,
                                color = Color(0xFF5E5C68).copy(alpha = 0.6f),
                                fontWeight = FontWeight.Light
                            )

                            Spacer(Modifier.height(8.dp))

                            // Go explore button
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(gold, Color(0xFFa87830))
                                        )
                                    )
                                    .clickable {
                                        navController.navigate(Routes.Home.route)
                                    }
                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "EXPLORE MOVIES",
                                    fontFamily = bebasNeue,
                                    fontSize = 15.sp,
                                    letterSpacing = 2.sp,
                                    color = Color(0xFF0A0A0F)
                                )
                            }
                        }
                    }
                } else {
                    // count
                    Text(
                        text = "${items.size} MOVIES SAVED",
                        fontFamily = bebasNeue,
                        fontSize = 12.sp,
                        letterSpacing = 2.sp,
                        color = Color(0xFF5E5C68),
                        modifier = Modifier.padding(
                            start = 18.dp,
                            bottom = 12.dp
                        )
                    )

                    // ── Grid ──
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            horizontal = 18.dp,
                            vertical = 8.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(items) { item ->
                            WatchlistCard(
                                item = item,
                                onMovieClick = {
                                    navController.navigate(
                                        Routes.MovieDetail.createRoute(item.tmdbId.toInt())
                                    )
                                },
                                onRemoveClick = {
                                    authViewModel.removeFromWatchlist(item.tmdbId)
                                }
                            )
                        }
                    }
                }
            }

            // ── Error ──
            is AuthViewModel.WatchlistState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚠ ${(watchlistState as AuthViewModel.WatchlistState.Error).message}",
                        color = Color(0xFFE84B4B),
                        fontSize = 13.sp
                    )
                }
            }

            else -> {}
        }
    }
}

// ── Watchlist Card ──
@Composable
fun WatchlistCard(
    item: WatchlistItem,
    onMovieClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    val gold = gold
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.67f)     // portrait ratio
            .clip(RoundedCornerShape(14.dp))
            .clickable { onMovieClick() }
    ) {
        // poster image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.posterUrl)
                .crossfade(true)
                .build(),
            contentDescription = item.movieTitle,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // remove button top right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(30.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable { onRemoveClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Remove",
                tint = Color(0xFFE84B4B),
                modifier = Modifier.size(15.dp)
            )
        }

        // bottom info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // genre pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(gold.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = item.genre.uppercase(),
                    fontSize = 8.sp,
                    color = gold,
                    letterSpacing = 1.sp
                )
            }

            // title
            Text(
                text = item.movieTitle.uppercase(),
                fontFamily = bebasNeue,
                fontSize = 14.sp,
                letterSpacing = 1.sp,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            // rating + year
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = gold,
                    modifier = Modifier.size(10.dp)
                )
                Text(
                    text = item.rating,
                    fontSize = 10.sp,
                    color = gold
                )
                Text(
                    text = "·",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.4f)
                )
                Text(
                    text = item.releaseYear,
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}
package com.example.cineversemovieapp.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cineversemovieapp.components.ReelVideoPlayer
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.TmdbState
import com.example.cineversemovieapp.viewmodel.TmdbViewModel
import kotlinx.coroutines.delay

@Composable
fun ReelsScreen(
    navController: NavController,
    tmdbViewModel: TmdbViewModel = viewModel()
) {
    val context = LocalContext.current
    val gold = gold
    val bg = Color(0xFF0A0A0F)

    val reelsState by tmdbViewModel.reelsMovies.collectAsState()
    val videoKeys by tmdbViewModel.reelsVideoKeys.collectAsState()

    val likedMovies = remember { mutableStateListOf<Int>() }
    val bookmarkedMovies = remember { mutableStateListOf<Int>() }

    LaunchedEffect(Unit) {
        if (reelsState is TmdbState.Idle) {
            tmdbViewModel.getReelsMovies()
        }
    }

    // List of sample videos that are known to work
    val demoVideos = listOf(
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        when (val state = reelsState) {
            is TmdbState.Loading, TmdbState.Idle -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = gold)
                }
            }
            is TmdbState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error loading reels", color = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { tmdbViewModel.getReelsMovies() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            is TmdbState.Success -> {
                val movies = state.movies
                if (movies.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No movies found", color = Color.White)
                    }
                } else {
                    val pagerState = rememberPagerState(pageCount = { movies.size })

                    VerticalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        beyondViewportPageCount = 1
                    ) { page ->
                        val movie = movies[page]
                        val videoUrl = demoVideos[page % demoVideos.size]
                        val trailerKey = videoKeys[movie.id]

                        LaunchedEffect(movie.id) {
                            if (trailerKey == null) {
                                tmdbViewModel.getMovieVideos(movie.id)
                            }
                        }

                        ReelCard(
                            movieId = movie.id,
                            title = movie.displayTitle,
                            rating = movie.rating,
                            genre = movie.getGenreName(),
                            overview = movie.overview,
                            year = movie.releaseYear,
                            backdropUrl = movie.backdropUrl,
                            isLiked = likedMovies.contains(movie.id),
                            isBookmarked = bookmarkedMovies.contains(movie.id),
                            videoUrl = videoUrl,
                            isPlaying = pagerState.currentPage == page,
                            trailerKey = trailerKey,
                            onLikeClick = {
                                if (likedMovies.contains(movie.id)) likedMovies.remove(movie.id)
                                else likedMovies.add(movie.id)
                            },
                            onBookmarkClick = {
                                if (bookmarkedMovies.contains(movie.id)) bookmarkedMovies.remove(movie.id)
                                else bookmarkedMovies.add(movie.id)
                            },
                            onMovieClick = {
                                navController.navigate(Routes.MovieDetail.createRoute(movie.id))
                            },
                            onTrailerClick = { key ->
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://www.youtube.com/watch?v=$key")
                                )
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReelCard(
    movieId: Int,
    title: String,
    rating: String,
    genre: String,
    overview: String,
    year: String,
    backdropUrl: String,
    isLiked: Boolean,
    isBookmarked: Boolean,
    videoUrl: String,
    isPlaying: Boolean,
    trailerKey: String?,
    onLikeClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onMovieClick: () -> Unit,
    onTrailerClick: (String) -> Unit
) {
    val gold = gold
    var showHeart by remember { mutableStateOf(false) }

    val heartScale by animateFloatAsState(
        targetValue = if (showHeart) 1.3f else 0f, label = "heartScale"
    )

    LaunchedEffect(showHeart) {
        if (showHeart) {
            delay(800)
            showHeart = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        showHeart = true
                        onLikeClick()
                    },
                    onTap = {
                        onMovieClick()
                    }
                )
            }
    ) {
        ReelVideoPlayer(
            videoUrl = videoUrl,
            backdropUrl = backdropUrl,
            isPlaying = isPlaying
        )

        // Gradient overlay for better text visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 400f
                    )
                )
        )

        AnimatedVisibility(
            visible = showHeart,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(100.dp)
                    .scale(heartScale)
            )
        }

        // Action Buttons
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReelActionButton(
                icon = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                label = "Like",
                tint = if (isLiked) Color.Red else Color.White,
                onClick = onLikeClick
            )

            ReelActionButton(
                icon = if (isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                label = "Save",
                tint = if (isBookmarked) gold else Color.White,
                onClick = onBookmarkClick
            )

            if (trailerKey != null) {
                ReelActionButton(
                    icon = Icons.Default.Videocam,
                    label = "Trailer",
                    tint = gold,
                    onClick = { onTrailerClick(trailerKey) }
                )
            }
        }

        // Movie Info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .padding(bottom = 60.dp)
                .fillMaxWidth(0.8f)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = gold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("$rating • $year", color = gold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = overview,
                color = Color.White.copy(alpha = 0.9f),
                maxLines = 3,
                fontSize = 14.sp,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ReelActionButton(icon: ImageVector, label: String, tint: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(label, color = Color.White, fontSize = 12.sp)
    }
}
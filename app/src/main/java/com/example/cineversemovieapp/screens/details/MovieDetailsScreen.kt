package com.example.cineversemovieapp.screens.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import com.example.cineversemovieapp.components.SceneDNAButton
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.AuthViewModel
import com.example.cineversemovieapp.viewmodel.CreditsState
import com.example.cineversemovieapp.viewmodel.MovieDetailState
import com.example.cineversemovieapp.viewmodel.TmdbState
import com.example.cineversemovieapp.viewmodel.TmdbViewModel

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    navController: NavController,
    tmdbViewModel: TmdbViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val gold = gold
    val bg = Color(0xFF0A0A0F)

    val movieDetailState by tmdbViewModel.movieDetail.collectAsState()
    val movieCreditsState by tmdbViewModel.movieCredits.collectAsState()
    val movieVideosState by tmdbViewModel.movieVideos.collectAsState()
    val watchlistIds by authViewModel.watchlistIds.collectAsState()
    val isBookmarked = watchlistIds.contains(movieId.toLong())
    val context = LocalContext.current

    LaunchedEffect(movieId) {
        tmdbViewModel.resetMovieDetail()
        tmdbViewModel.resetMovieCredits()
        tmdbViewModel.resetMovieVideos()
        tmdbViewModel.getMovieDetails(movieId)
        tmdbViewModel.getMovieCredits(movieId)
        tmdbViewModel.getMovieVideos(movieId)
        authViewModel.getWatchlist()
    }

    val trendingState by tmdbViewModel.trendingMovies.collectAsState()
    val similarMovies = (trendingState as? TmdbState.Success)?.movies
        ?.filter { it.id != movieId }
        ?.take(10) ?: emptyList()

    when (movieDetailState) {
        is MovieDetailState.Loading,
        MovieDetailState.Idle -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = gold)
            }
        }

        is MovieDetailState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚠ ${(movieDetailState as MovieDetailState.Error).message}",
                    color = Color(0xFFE84B4B),
                    fontSize = 13.sp
                )
            }
        }

        is MovieDetailState.Success -> {
            val movie = (movieDetailState as MovieDetailState.Success).movie
            val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
            val surface = Color(0xFF1A1A24)
            val muted = Color(0xFF5E5C68)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg)
                    .verticalScroll(rememberScrollState())
            ) {

                // ── BACKDROP ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie.backdropUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.85f),
                                        bg
                                    )
                                )
                            )
                    )

                    // Back button
                    Box(
                        modifier = Modifier
                            .padding(top = 52.dp, start = 18.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                            .align(Alignment.TopStart)
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

                    // ── Bookmark Button ──
                    Box(
                        modifier = Modifier
                            .padding(top = 52.dp, end = 18.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                            .align(Alignment.TopEnd)
                            .clickable {
                                if (isBookmarked) {
                                    authViewModel.removeFromWatchlist(movieId.toLong())
                                } else {
                                    authViewModel.addToWatchlist(
                                        tmdbId = movieId.toLong(),
                                        title = movie.title ?: "",
                                        posterUrl = movie.posterUrl,
                                        backdropUrl = movie.backdropUrl,
                                        rating = movie.rating,
                                        releaseYear = movie.releaseYear,
                                        genre = movie.genres.firstOrNull()?.name ?: "Movie"
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark
                            else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = gold,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // IMDb badge
                    Box(
                        modifier = Modifier
                            .padding(top = 56.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(gold)
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        Text(
                            text = "IMDb",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = bg,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // ── CONTENT ──
                Column(modifier = Modifier.padding(horizontal = 18.dp)) {

                    Spacer(modifier = Modifier.height(14.dp))

                    // ── Genre Pills ──
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        items(movie.genres.take(3)) { genre ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .border(
                                        1.dp,
                                        gold.copy(alpha = 0.3f),
                                        RoundedCornerShape(100.dp)
                                    )
                                    .background(gold.copy(alpha = 0.08f))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = genre.name.uppercase(),
                                    fontSize = 11.sp,
                                    color = gold,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // ── Title ──
                    Text(
                        text = (movie.title ?: "Unknown").uppercase(),
                        fontFamily = bebasNeue,
                        fontSize = 36.sp,
                        letterSpacing = 3.sp,
                        color = Color.White,
                        lineHeight = 38.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // ── Meta Row ──
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
                            contentDescription = null,
                            tint = muted,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(text = movie.duration, fontSize = 12.sp, color = muted)
                        MetaDot()
                        Text(text = movie.releaseYear, fontSize = 12.sp, color = muted)
                        MetaDot()
                        Icon(
                            imageVector = Icons.Filled.Language,
                            contentDescription = null,
                            tint = muted,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(text = movie.language, fontSize = 12.sp, color = muted)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Rating Cards ──
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RatingCard(
                            label = "IMDb",
                            value = movie.rating,
                            sub = "★★★★☆",
                            subColor = gold,
                            modifier = Modifier.weight(1f)
                        )
                        RatingCard(
                            label = "Release",
                            value = movie.releaseYear,
                            sub = "In Cinemas",
                            subColor = muted,
                            modifier = Modifier.weight(1f)
                        )
                        RatingCard(
                            label = "Votes",
                            value = movie.voteCountFormatted,
                            sub = "Reviews",
                            subColor = muted,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Action Buttons ──
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Watch Now
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            gold,
                                            Color(0xFFa87830)
                                        )
                                    )
                                )
                                .clickable {
                                    val videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                                    navController.navigate(
                                        Routes.MoviePlayer.createRoute(
                                            title = movie.title ?: "Movie",
                                            year = movie.releaseYear,
                                            url = videoUrl
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                    tint = bg,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "WATCH NOW",
                                    fontFamily = bebasNeue,
                                    fontSize = 16.sp,
                                    letterSpacing = 2.sp,
                                    color = bg
                                )
                            }
                        }

                        // Trailer
                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(surface)
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.07f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    val trailer = (movieVideosState as? TmdbViewModel.VideosState.Success)
                                        ?.videos
                                        ?.firstOrNull()

                                    if (trailer != null) {
                                        val appIntent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("vnd.youtube:${trailer.key}")
                                        )
                                        val webIntent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(trailer.youtubeUrl)
                                        )
                                        try {
                                            context.startActivity(appIntent)
                                        } catch (e: Exception) {
                                            context.startActivity(webIntent)
                                        }
                                    }
                                }
                                .padding(horizontal = 18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Videocam,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "TRAILER",
                                    fontFamily = bebasNeue,
                                    fontSize = 16.sp,
                                    letterSpacing = 2.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── Scene DNA ──
                    SceneDNAButton(movieId = movieId, navController = navController)

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Overview ──
                    Row {
                        Text(
                            text = "OVER",
                            fontFamily = bebasNeue,
                            fontSize = 18.sp,
                            color = Color.White,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "VIEW",
                            fontFamily = bebasNeue,
                            fontSize = 18.sp,
                            color = gold,
                            letterSpacing = 2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = movie.overview.ifEmpty { "No overview available." },
                        fontSize = 13.sp,
                        color = muted,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Light
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Top Cast ──
                    Row {
                        Text(
                            text = "TOP ",
                            fontFamily = bebasNeue,
                            fontSize = 18.sp,
                            color = Color.White,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "CAST",
                            fontFamily = bebasNeue,
                            fontSize = 18.sp,
                            color = gold,
                            letterSpacing = 2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    when (movieCreditsState) {
                        is CreditsState.Loading -> {
                            CircularProgressIndicator(
                                color = gold,
                                strokeWidth = 2.dp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        is CreditsState.Success -> {
                            val castList = (movieCreditsState as CreditsState.Success).cast
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                items(castList) { castMember ->
                                    CastCard(
                                        name = castMember.name,
                                        role = castMember.character,
                                        avatarUrl = castMember.profileUrl
                                    )
                                }
                            }
                        }

                        is CreditsState.Error -> {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(placeholderCast) { castMember ->
                                    CastCard(
                                        name = castMember.first,
                                        role = castMember.second,
                                        avatarUrl = castMember.third
                                    )
                                }
                            }
                        }

                        else -> {}
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
                    Spacer(modifier = Modifier.height(20.dp))

                    // ── More Like This ──
                    Row {
                        Text(
                            text = "MORE ",
                            fontFamily = bebasNeue,
                            fontSize = 18.sp,
                            color = Color.White,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "LIKE THIS",
                            fontFamily = bebasNeue,
                            fontSize = 18.sp,
                            color = gold,
                            letterSpacing = 2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        items(similarMovies) { similarMovie ->
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(145.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        navController.navigate(Routes.MovieDetail.createRoute(similarMovie.id))
                                    }
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(similarMovie.posterUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = similarMovie.displayTitle,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.8f)
                                                )
                                            )
                                        )
                                )

                                Text(
                                    text = similarMovie.displayTitle,
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }

        else -> {}
    }
}

@Composable
fun CastCard(name: String, role: String, avatarUrl: String) {
    val muted = Color(0xFF5E5C68)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White.copy(alpha = 0.07f), CircleShape)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = name,
            fontSize = 10.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(64.dp),
            lineHeight = 14.sp
        )

        Text(
            text = role,
            fontSize = 9.sp,
            color = muted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(64.dp)
        )
    }
}

@Composable
fun MetaDot() {
    Box(
        modifier = Modifier
            .size(3.dp)
            .clip(CircleShape)
            .background(Color(0xFF5E5C68))
    )
}

@Composable
fun RatingCard(
    label: String,
    value: String,
    sub: String,
    subColor: Color,
    modifier: Modifier = Modifier
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val gold = gold

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A24))
            .border(1.dp, Color.White.copy(alpha = 0.07f), RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = label, fontSize = 10.sp, color = Color(0xFF5E5C68), letterSpacing = 1.sp)
        Text(
            text = value,
            fontFamily = bebasNeue,
            fontSize = 22.sp,
            color = gold,
            letterSpacing = 1.sp
        )
        Text(text = sub, fontSize = 10.sp, color = subColor, letterSpacing = 1.sp)
    }
}

val placeholderCast = listOf(
    Triple("Actor One", "Lead Role", "https://i.pravatar.cc/64?img=11"),
    Triple("Actor Two", "Support", "https://i.pravatar.cc/64?img=5"),
    Triple("Actor Three", "Lead Role", "https://i.pravatar.cc/64?img=9"),
    Triple("Actor Four", "Support", "https://i.pravatar.cc/64?img=15"),
    Triple("Actor Five", "Cameo", "https://i.pravatar.cc/64?img=22")
)

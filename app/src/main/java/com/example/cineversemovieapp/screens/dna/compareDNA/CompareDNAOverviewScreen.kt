package com.example.cineversemovieapp.screens.dna.compareDNA

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.data.tmdb.TmdbMovie
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.MovieDetailState
import com.example.cineversemovieapp.viewmodel.TmdbState
import com.example.cineversemovieapp.viewmodel.TmdbViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareDNAScreen(
    movieId: Int,
    navController: NavController,
    tmdbViewModel: TmdbViewModel
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val goldColor = gold
    val bg = Color(0xFF0A0A0F)
    val subtitleGray = Color(0xFF818182)
    val cardBg = Color(0xFF111118)

    var searchQuery by remember { mutableStateOf("") }
    var selectedMovie by remember { mutableStateOf<TmdbMovie?>(null) }

    val movieDetailState by tmdbViewModel.movieDetail.collectAsState()
    val recommendationsState by tmdbViewModel.recommendations.collectAsState()

    LaunchedEffect(movieId) {
        tmdbViewModel.getMovieDetails(movieId)
        tmdbViewModel.getRecommendations(movieId)
    }

    Scaffold(
        containerColor = bg,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Button(
                    onClick = { 
                        selectedMovie?.let { target ->
                            navController.navigate(Routes.RealCompareDNA.createRoute(movieId, target.id))
                        }
                    },
                    enabled = selectedMovie != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = goldColor,
                        disabledContainerColor = goldColor.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = "Compare Now",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = bebasNeue,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 25.dp),
                text = "Compare DNA",
                fontSize = 24.sp,
                letterSpacing = 1.sp,
                color = goldColor,
                fontFamily = bebasNeue,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Base Movie Card ──
            when (movieDetailState) {
                is MovieDetailState.Success -> {
                    val movie = (movieDetailState as MovieDetailState.Success).movie
                    BaseMovieSelectionCard(
                        title = movie.displayTitle,
                        posterUrl = movie.posterUrl,
                        subtitle = "Comparing against",
                        goldColor = goldColor,
                        cardBg = cardBg
                    )
                }
                is MovieDetailState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(110.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = goldColor)
                    }
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Search Bar ──
            CompareSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                cardBg = cardBg
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "SUGGESTED MOVIES",
                fontSize = 13.sp,
                letterSpacing = 2.sp,
                color = subtitleGray,
                fontFamily = bebasNeue
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (recommendationsState) {
                is TmdbState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = goldColor)
                    }
                }
                is TmdbState.Success -> {
                    val movies = (recommendationsState as TmdbState.Success).movies
                        .filter { it.displayTitle.contains(searchQuery, ignoreCase = true) }
                    
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(movies) { tmdbMovie ->
                            SuggestedMovieSelectionCard(
                                tmdbMovie = tmdbMovie,
                                isSelected = selectedMovie?.id == tmdbMovie.id,
                                onSelect = { selectedMovie = tmdbMovie },
                                goldColor = goldColor,
                                cardBg = cardBg
                            )
                        }
                    }
                }
                is TmdbState.Error -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text(text = "Failed to load suggestions", color = Color.Red)
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun BaseMovieSelectionCard(
    title: String,
    posterUrl: String,
    subtitle: String,
    goldColor: Color,
    cardBg: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(posterUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Surface(
                color = goldColor.copy(alpha = 0.12f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, goldColor.copy(alpha = 0.35f))
            ) {
                Text(
                    text = "Base",
                    color = goldColor,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    cardBg: Color
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(14.dp)),
        placeholder = {
            Text(
                text = "Search movie title...",
                color = Color.Gray.copy(alpha = 0.6f),
                fontSize = 15.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.4f)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = cardBg,
            unfocusedContainerColor = cardBg,
            disabledContainerColor = cardBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = gold
        ),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
    )
}

@Composable
fun SuggestedMovieSelectionCard(
    tmdbMovie: TmdbMovie,
    isSelected: Boolean,
    onSelect: () -> Unit,
    goldColor: Color,
    cardBg: Color
) {
    val borderColor = if (isSelected) goldColor.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.05f)
    val tagColor = if (isSelected) goldColor else Color(0xFF64B5F6)
    val tagBg = if (isSelected) goldColor.copy(alpha = 0.12f) else Color(0xFF1A1A24)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(tmdbMovie.posterUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = tmdbMovie.displayTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tmdbMovie.displayTitle,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp
                )
                Text(
                    text = "${tmdbMovie.getGenreName()} · ${tmdbMovie.releaseYear}",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Surface(
                color = tagBg,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, if (isSelected) goldColor.copy(alpha = 0.45f) else Color.White.copy(alpha = 0.12f))
            ) {
                Text(
                    text = if (isSelected) "Selected" else tmdbMovie.getGenreName(),
                    color = tagColor,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

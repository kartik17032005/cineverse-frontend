package com.example.cineversemovieapp.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.models.AiMovieResult
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.AiState
import com.example.cineversemovieapp.viewmodel.AiViewModel

@Composable
fun AiRecommendScreen(
    navController: NavController,
    aiViewModel: AiViewModel = viewModel()
) {
    val gold = gold
    val bg = Color(0xFF0A0A0F)
    val surface = Color(0xFF1A1A24)
    val muted = Color(0xFF5E5C68)
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val keyboard = LocalSoftwareKeyboardController.current

    val aiState by aiViewModel.aiState.collectAsState()
    var query by remember { mutableStateOf("") }

    // suggestion chips for quick queries
    val suggestions = listOf(
        "Like Interstellar but less depressing",
        "Funny movies for a bad day",
        "Thriller with a plot twist",
        "Feel good romantic movies",
        "Mind bending sci-fi"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {

        // ── Header ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 52.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // back button
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(surface)
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

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // AI sparkle icon
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = gold,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "AI RECOMMEND",
                        fontFamily = bebasNeue,
                        fontSize = 22.sp,
                        letterSpacing = 3.sp,
                        color = Color.White
                    )
                }
                Text(
                    text = "Describe what you're in the mood for",
                    fontSize = 12.sp,
                    color = muted,
                    fontWeight = FontWeight.Light
                )
            }
        }

        // ── Input Box ──
        Column(
            modifier = Modifier.padding(horizontal = 18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(surface)
                    .border(
                        1.dp,
                        gold.copy(alpha = 0.3f),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // text input
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    ),
                    cursorBrush = SolidColor(gold),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboard?.hide()
                            aiViewModel.getRecommendations(query)
                        }
                    ),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = "e.g. Something like Interstellar but happier...",
                                fontSize = 14.sp,
                                color = muted.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Light
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(10.dp))

                // send button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(gold, Color(0xFFa87830)))
                        )
                        .clickable {
                            keyboard?.hide()
                            aiViewModel.getRecommendations(query)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Search",
                        tint = bg,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Content based on state ──
        when (aiState) {

            // ── Idle — show suggestions ──
            is AiState.Idle -> {
                Text(
                    text = "TRY THESE",
                    fontFamily = bebasNeue,
                    fontSize = 13.sp,
                    letterSpacing = 2.sp,
                    color = muted,
                    modifier = Modifier.padding(start = 18.dp, bottom = 12.dp)
                )

                // suggestion chips
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    suggestions.forEach { suggestion ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(surface)
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.06f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    query = suggestion
                                    aiViewModel.getRecommendations(suggestion)
                                }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // sparkle icon
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = gold.copy(alpha = 0.6f),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = suggestion,
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }
            }

            // ── Loading ──
            is AiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(color = gold)

                        Text(
                            text = "AI is thinking...",
                            fontFamily = bebasNeue,
                            fontSize = 18.sp,
                            letterSpacing = 2.sp,
                            color = muted
                        )

                        Text(
                            text = "Finding perfect movies for you 🎬",
                            fontSize = 13.sp,
                            color = muted.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }

            // ── Results ──
            is AiState.Success -> {
                val movies = (aiState as AiState.Success).movies

                // results header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AI PICKS FOR YOU",
                        fontFamily = bebasNeue,
                        fontSize = 16.sp,
                        letterSpacing = 2.sp,
                        color = Color.White
                    )

                    // search again button
                    Text(
                        text = "Try Again",
                        fontSize = 12.sp,
                        color = gold,
                        modifier = Modifier.clickable {
                            aiViewModel.reset()
                            query = ""
                        }
                    )
                }

                // movie list
                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = 18.dp,
                        vertical = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(movies) { movie ->
                        AiMovieCard(
                            movie = movie,
                            onMovieClick = {
                                navController.navigate(
                                    Routes.MovieDetail.createRoute(movie.tmdbId)
                                )
                            }
                        )
                    }
                }
            }

            // ── Error ──
            is AiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "😕", fontSize = 48.sp)
                        Text(
                            text = "SOMETHING WENT WRONG",
                            fontFamily = bebasNeue,
                            fontSize = 18.sp,
                            letterSpacing = 2.sp,
                            color = Color(0xFFE84B4B)
                        )
                        Text(
                            text = (aiState as AiState.Error).message,
                            fontSize = 12.sp,
                            color = muted,
                            fontWeight = FontWeight.Light
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(gold.copy(alpha = 0.1f))
                                .border(1.dp, gold.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .clickable { aiViewModel.reset() }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Try Again",
                                fontSize = 13.sp,
                                color = gold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── AI Movie Card ──
@Composable
fun AiMovieCard(
    movie: AiMovieResult,
    onMovieClick: () -> Unit
) {
    val gold = gold
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val surface = Color(0xFF1A1A24)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(surface)
            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(14.dp))
            .clickable { onMovieClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // poster
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 115.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF2A2A34))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.posterUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // info
        Column(
            modifier = Modifier
                .weight(1f)
                .height(115.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = movie.title.uppercase(),
                    fontFamily = bebasNeue,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // rating + year
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = gold,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "%.1f".format(movie.rating),
                        fontSize = 11.sp,
                        color = gold
                    )
                    Text(
                        text = "·",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.3f)
                    )
                    Text(
                        text = movie.releaseYear,
                        fontSize = 11.sp,
                        color = Color(0xFF5E5C68)
                    )
                }
            }

            // AI reason box — why Claude picked this
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(gold.copy(alpha = 0.08f))
                    .border(
                        0.5.dp,
                        gold.copy(alpha = 0.2f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = gold,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = movie.aiReason,
                        fontSize = 11.sp,
                        color = gold.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Light,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
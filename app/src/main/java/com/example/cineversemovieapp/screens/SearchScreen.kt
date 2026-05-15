package com.example.cineversemovieapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.components.AI_Movie_Finder
import com.example.cineversemovieapp.components.SearchBar
import com.example.cineversemovieapp.components.trendingNow.TrendingNowCard
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.TmdbState
import com.example.cineversemovieapp.viewmodel.TmdbViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    navController: NavController,
    tmdbViewModel: TmdbViewModel = viewModel()
) {
    val gold = gold
    val bg = Color(0xFF0A0A0F)
    val muted = Color(0xFF5E5C68)
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    val searchQuery = remember { mutableStateOf("") }
    val searchState by tmdbViewModel.searchResults.collectAsState()

    // ── Debounced search ──
    LaunchedEffect(searchQuery.value) {
        if (searchQuery.value.isBlank()) {
            tmdbViewModel.resetSearchResults()
            return@LaunchedEffect
        }
        delay(500)
        tmdbViewModel.searchMovies(searchQuery.value)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(top = 52.dp)
    ) {
        // --- Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Back Button
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

            Text(
                text = "SEARCH",
                fontFamily = bebasNeue,
                fontSize = 32.sp,
                letterSpacing = 4.sp,
                color = Color.White
            )
        }

        SearchBar(
            query = searchQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            searchQuery.value.isBlank() -> {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    AI_Movie_Finder(navController = navController)
                    Spacer(modifier = Modifier.height(32.dp))
                    EmptySearchState(
                        bebasNeue = bebasNeue,
                        muted = muted,
                        gold = gold,
                        searchQuery = searchQuery
                    )
                }
            }

            searchState is TmdbState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = gold)
                }
            }

            searchState is TmdbState.Success -> {
                val movies = (searchState as TmdbState.Success).movies

                if (movies.isEmpty()) {
                    NoResultsFound(bebasNeue, muted)
                } else {
                    Text(
                        text = "${movies.size} RESULTS FOR \"${searchQuery.value.uppercase()}\"",
                        fontFamily = bebasNeue,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp,
                        color = gold,
                        modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(movies) { movie ->
                            TrendingNowCard(
                                movie = movie,
                                rank = movies.indexOf(movie) + 1,
                                navController = navController
                            )
                        }
                    }
                }
            }

            searchState is TmdbState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "⚠ ${(searchState as TmdbState.Error).message}",
                        color = Color(0xFFE84B4B),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun NoResultsFound(bebasNeue: FontFamily, muted: Color) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🔍", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "NO MOVIES FOUND",
                fontFamily = bebasNeue,
                fontSize = 22.sp,
                color = Color.White,
                letterSpacing = 2.sp
            )
            Text(
                text = "We couldn't find anything matching that title",
                fontSize = 13.sp,
                color = muted,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun EmptySearchState(
    bebasNeue: FontFamily,
    muted: Color,
    gold: Color,
    searchQuery: MutableState<String>
) {
    val genres = listOf(
        "Action" to 28, "Sci-Fi" to 878, "Comedy" to 35, 
        "Drama" to 18, "Horror" to 27, "Romance" to 1074, 
        "Thriller" to 53, "Animation" to 16
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "POPULAR GENRES",
            fontFamily = bebasNeue,
            fontSize = 18.sp,
            letterSpacing = 2.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val chunks = genres.chunked(3)
            chunks.forEach { rowGenres ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    rowGenres.forEach { (name, id) ->
                        GenreChip(
                            genreName = name,
                            isSelected = false,
                            onClick = { searchQuery.value = name }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "QUICK TIPS",
            fontFamily = bebasNeue,
            fontSize = 18.sp,
            letterSpacing = 2.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        listOf(
            "Browse by your favorite actors",
            "Find hidden gems using the AI Finder",
            "Try searching by director names"
        ).forEach { tip ->
            Row(
                modifier = Modifier.padding(bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(gold))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = tip, fontSize = 14.sp, color = muted, fontWeight = FontWeight.Light)
            }
        }
    }
}

@Composable
fun GenreChip(genreName: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) gold else Color(0xFF1A1A24))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
    ) {
        Text(
            text = genreName,
            fontSize = 13.sp,
            color = if (isSelected) Color.Black else Color.White.copy(alpha = 0.9f)
        )
    }
}

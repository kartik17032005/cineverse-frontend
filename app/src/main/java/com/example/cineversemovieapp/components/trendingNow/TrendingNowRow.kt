package com.example.cineversemovieapp.components.trendingNow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cineversemovieapp.data.tmdb.TmdbMovie

@Composable
fun TrendingNowRow(
    movies: List<TmdbMovie>,
    navController: NavController
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(movies) { index, movie ->
            TrendingNowCard(
                movie = movie,
                rank = index+1,
                navController = navController
            )
        }
    }
}
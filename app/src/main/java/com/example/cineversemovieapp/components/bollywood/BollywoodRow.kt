package com.example.cineversemovieapp.components.bollywood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cineversemovieapp.data.TmdbMovie

@Composable
fun BollywoodRow(
    movies: List<TmdbMovie>,
    navController: NavController
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(movies) { index, movie ->
            BollywoodCards(
                movie = movie,
                rank = index + 1,
                navController = navController
            )
        }
    }
}
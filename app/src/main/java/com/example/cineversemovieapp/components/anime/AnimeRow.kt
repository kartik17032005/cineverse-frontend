package com.example.cineversemovieapp.components.anime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cineversemovieapp.data.TmdbTvShow

@Composable
fun AnimeRow(
    shows: List<TmdbTvShow>,
    navController: NavController
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(shows) { index, show ->
            AnimeCard(
                show = show,
                rank = index + 1,
                navController
            )
        }
    }
}
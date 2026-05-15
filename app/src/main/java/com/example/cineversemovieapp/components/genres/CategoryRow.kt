package com.example.cineversemovieapp.components.genres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun CategoryRow(
    genres: List<String>,
    selectedGenre: String,
    onGenreSelected: (String) -> Unit
){
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 18.dp)
    ) {
        items(genres) {genre ->
            GenrePill(
                genre = genre,
                isSelected = genre == selectedGenre,
                onClick = { onGenreSelected(genre) }
            )
        }
    }
}
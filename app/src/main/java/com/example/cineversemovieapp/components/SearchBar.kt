package com.example.cineversemovieapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBar(
    query: MutableState<String>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onSearch: (String) -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    val gold = Color(0xFFCCA246)
    val muted = Color(0xFF5E5C68)
    val surfaceColor = Color(0xFF1A1A24)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(surfaceColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = muted,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        BasicTextField(
            value = query.value,
            onValueChange = { query.value = it },
            modifier = Modifier.weight(1f),
            enabled = enabled,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 15.sp
            ),
            cursorBrush = SolidColor(gold),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(query.value) }),
            decorationBox = { innerTextField ->
                if (query.value.isEmpty()) {
                    Text(
                        text = "Search movies, series...",
                        color = muted,
                        fontSize = 15.sp
                    )
                }
                innerTextField()
            }
        )

        if (enabled) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(gold)
                    .clickable { onFilterClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}



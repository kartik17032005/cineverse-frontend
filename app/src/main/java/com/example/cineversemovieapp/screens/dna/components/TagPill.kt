package com.example.cineversemovieapp.screens.dna.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineversemovieapp.components.EmotionTag

@Composable
fun TagPill(
    tag: EmotionTag,
    gold: Color,
    goldFaint: Color,
    goldBorder: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(goldFaint)
            .border(1.dp, goldBorder, RoundedCornerShape(20.dp)) // needs import below
            .padding(8.dp)
            .padding(start = 6.dp, end = 6.dp)
    ) {
        Text(
            text = "${tag.emoji} ${tag.label}",
            color = gold,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
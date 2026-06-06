package com.example.cineversemovieapp.screens.dna.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Waves
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cineversemovieapp.data.remote.dto.PeakMoment

@Composable
fun PeakMomentsSection(peakMoments: List<PeakMoment>, goldColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        peakMoments.forEach { moment ->
            val icon = when {
                moment.type.contains("Tension", true) || moment.title.contains("wave", true) -> Icons.Default.Waves
                moment.type.contains("Action", true) || moment.title.contains("storm", true) -> Icons.Default.Language
                else -> Icons.Default.ChatBubble
            }
            PeakMomentItem(moment, icon, goldColor)
        }
    }
}

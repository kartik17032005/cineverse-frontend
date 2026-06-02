package com.example.cineversemovieapp.data.badge

import androidx.compose.ui.graphics.Color
import com.example.cineversemovieapp.enums.BadgeTier
import com.example.cineversemovieapp.enums.BadgeType

data class CineBadge (
    val id: String,
    val name: String,
    val description: String,
    val type: BadgeType,
    val ringColor: Color,
    val tier: BadgeTier,
    val progress: Int,
    val goal: Int,
    val isUnlocked: Boolean = progress >= goal
)
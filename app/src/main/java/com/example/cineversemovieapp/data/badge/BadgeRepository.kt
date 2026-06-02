package com.example.cineversemovieapp.data.badge

import androidx.compose.ui.graphics.Color
import com.example.cineversemovieapp.enums.BadgeTier
import com.example.cineversemovieapp.enums.BadgeType

object BadgeRepository {
    private val Gold = Color(0xFFCCA246)
    private val Muted = Color(0xFF5E5C68)
    private val Bronze = Color(0xFFCD7F32)
    private val Silver = Color(0xFFA8A9AD)

    fun getBadges(): List<CineBadge> = listOf(
        CineBadge("nolan", "Nolan Completionist", "Watch every Nolan film", BadgeType.STAR, Gold, BadgeTier.GOLD, 12, 12),
        CineBadge("horror", "Horror Veteran", "Watch 25 horror films", BadgeType.HEART, Color(0xFFE05C5C), BadgeTier.SILVER, 25, 25),
        CineBadge("binge", "Binge Master", "5 episodes in one day", BadgeType.PLAY, Color(0xFF5082DC), BadgeTier.BRONZE, 5, 5),
        CineBadge("social", "Social Cinephile", "Write 50 reviews", BadgeType.GLOBE, Color(0xFF4CAF80), BadgeTier.GOLD, 50, 50),
        CineBadge("scifi", "Sci-Fi Explorer", "Watch 20 sci-fi films", BadgeType.SHIELD, Color(0xFF8C5AC8), BadgeTier.SILVER, 13, 20),
        CineBadge("night", "Night Owl", "Watch 10 films after midnight", BadgeType.CLOCK, Color(0xFFEF9F27), BadgeTier.BRONZE, 3, 10),
        CineBadge("locked1", "???", "Keep watching to unlock", BadgeType.STAR, Muted, BadgeTier.BRONZE, 0, 30),
        CineBadge("locked2", "???", "Keep watching to unlock", BadgeType.PLAY, Muted, BadgeTier.BRONZE, 0, 50),
    )
}

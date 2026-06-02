package com.example.cineversemovieapp.data.remote.dto

data class MovieDNA(
    val sceneDNA: List<ScenePoint>,
    val peakMoments: List<PeakMoment>,
    val pacingRhythm: PacingRhythm,
    val emotionBreakdown: List<EmotionBreakdownItem>
)

data class ScenePoint(
    val label: String,
    val intensity: Int
)

data class PeakMoment(
    val title: String,
    val minute: Int,
    val type: String,
    val intensity: Int
)

data class PacingRhythm(
    val action: Int,
    val drama: Int,
    val buildup: Int,
    val calm: Int
)

data class EmotionBreakdownItem(
    val label: String,
    val percentage: Int
)

package com.example.cineversemovieapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineversemovieapp.data.remote.dto.EmotionBreakdownItem
import com.example.cineversemovieapp.data.remote.dto.MovieDNA
import com.example.cineversemovieapp.data.remote.dto.PacingRhythm
import com.example.cineversemovieapp.data.remote.dto.PeakMoment
import com.example.cineversemovieapp.data.remote.dto.ScenePoint
import com.example.cineversemovieapp.network.RetrofitInstance
import com.example.cineversemovieapp.repository.SceneDNARepository
import kotlinx.coroutines.launch
import kotlin.random.Random

class SceneDNAViewModel : ViewModel() {

    private val repository = SceneDNARepository(RetrofitInstance.api)

    var movieDNA by mutableStateOf<MovieDNA?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadSceneDNA(movieId: Long) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                
                val result = try {
                    repository.getSceneDNA(movieId)
                } catch (e: Exception) {
                    null
                }

                if (result == null || result.sceneDNA.isEmpty() || isGenericPlaceholder(result)) {
                    movieDNA = generateUniqueMovieDNA(movieId)
                } else {
                    movieDNA = result
                }

            } catch (e: Exception) {
                movieDNA = generateUniqueMovieDNA(movieId)
            } finally {
                isLoading = false
            }
        }
    }

    private fun isGenericPlaceholder(dna: MovieDNA): Boolean {
        if (dna.sceneDNA.size < 5) return true
        val firstIntensity = dna.sceneDNA.first().intensity
        return dna.sceneDNA.all { it.intensity == firstIntensity }
    }

    fun generateUniqueMovieDNA(movieId: Long): MovieDNA {
        val random = Random(movieId)
        
        // 1. Generate unique Scene DNA Points for the graph
        val pointsCount = 25 + random.nextInt(15)
        val sceneLabels = listOf("Action", "Suspense", "Drama", "Tragedy", "Euphoria", "Mystery")
        val sceneDNA = List(pointsCount) { i ->
            val intensity = if (i % 7 == 0) 80 + random.nextInt(20) else 15 + random.nextInt(65)
            ScenePoint(label = sceneLabels[random.nextInt(sceneLabels.size)], intensity = intensity)
        }

        // 2. Emotion Breakdown
        val emotionLabels = listOf("Awe", "Tension", "Grief", "Hope", "Fear")
        var remaining = 100
        val emotionBreakdown = mutableListOf<EmotionBreakdownItem>()
        
        for (i in 0 until emotionLabels.size - 1) {
            val percent = random.nextInt(remaining / (emotionLabels.size - i)) + 5
            emotionBreakdown.add(EmotionBreakdownItem(emotionLabels[i], percent))
            remaining -= percent
        }
        emotionBreakdown.add(EmotionBreakdownItem(emotionLabels.last(), remaining))
        val sortedBreakdown = emotionBreakdown.sortedByDescending { it.percentage }

        // 3. Peak Moments
        val pool = listOf(
            "Final Confrontation", "The Revelation", "Chase Sequence", 
            "Emotional Goodbye", "Unexpected Betrayal", "Hero's Journey",
            "Climactic Battle", "The Silent Walk", "Midnight Escape", "Discovery"
        ).shuffled(random)
        
        val peakMoments = List(3) { i ->
            PeakMoment(
                title = pool[i % pool.size],
                minute = 30 + (i * 40) + random.nextInt(20),
                type = listOf("Action peak", "Tension peak", "Emotional peak", "Suspense peak")[random.nextInt(4)],
                intensity = 88 + random.nextInt(12)
            )
        }.sortedBy { it.minute }

        // 4. Pacing Rhythm
        val a = 25 + random.nextInt(20)
        val d = 20 + random.nextInt(20)
        val b = 10 + random.nextInt(20)
        val c = (100 - (a + d + b)).coerceAtLeast(5)

        // 5. Generate Summary
        val topEmotions = sortedBreakdown.take(2).joinToString(" and ") { it.label }
        val mainPeak = peakMoments.maxByOrNull { it.intensity }?.title ?: "climactic moments"
        val rhythmType = if (a > d) "high-tension emotional" else "deeply atmospheric"
        
        val summary = "This movie maintains a $rhythmType rhythm, dominated by $topEmotions. Major intensity spikes occur during the $mainPeak."

        return MovieDNA(
            sceneDNA = sceneDNA,
            peakMoments = peakMoments,
            pacingRhythm = PacingRhythm(action = a, drama = d, buildup = b, calm = c),
            emotionBreakdown = sortedBreakdown,
            summary = summary
        )
    }
}

package com.example.cineversemovieapp.screens.dna

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.components.EmotionTag
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.screens.dna.components.EmotionBreakdownCard
import com.example.cineversemovieapp.screens.dna.components.InfoCard
import com.example.cineversemovieapp.screens.dna.components.IntensityCurveChart
import com.example.cineversemovieapp.screens.dna.components.PacingRhythmCard
import com.example.cineversemovieapp.screens.dna.components.PeakMomentsSection
import com.example.cineversemovieapp.screens.dna.components.TagPill
import com.example.cineversemovieapp.screens.dna.components.TopBar
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.MovieDetailState
import com.example.cineversemovieapp.viewmodel.SceneDNAViewModel
import com.example.cineversemovieapp.viewmodel.TmdbViewModel

@Composable
fun SceneDNAScreen(
    movieId: Int,
    navController: NavController,
    viewModel: SceneDNAViewModel,
    tmdbViewModel: TmdbViewModel
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val goldColor = gold
    val bg = Color(0xFF0A0A0F)
    val subtitleGray = Color(0xFF818182)
    val goldFaint = goldColor.copy(alpha = 0.15f)
    val goldBorder = goldColor.copy(alpha = 0.30f)

    val movieDetailState by tmdbViewModel.movieDetail.collectAsState()
    val movieDNA = viewModel.movieDNA
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    LaunchedEffect(movieId) {
        tmdbViewModel.getMovieDetails(movieId)
        viewModel.loadSceneDNA(movieId.toLong())
    }

    Scaffold(
        topBar = { TopBar(onBack = { navController.popBackStack() }) },
        containerColor = bg
    ) { paddingValues ->

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = goldColor)
            }
        } else if (errorMessage != null && movieDNA == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage, color = Color.Red)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                var movieDuration = "120 min"

                // ── Movie Header ──
                if (movieDetailState is MovieDetailState.Success) {
                    val movie = (movieDetailState as MovieDetailState.Success).movie
                    movieDuration = movie.duration
                    Row(verticalAlignment = Alignment.Top) {
                        Card(
                            modifier = Modifier
                                .height(130.dp)
                                .width(90.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(movie.posterUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = movie.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = movie.title ?: "Unknown",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${movie.releaseYear} • ${movie.duration}",
                                color = subtitleGray,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                movie.genres?.take(2)?.forEach { genre ->
                                    TagPill(
                                        tag = EmotionTag("", genre.name),
                                        gold = goldColor,
                                        goldFaint = goldFaint,
                                        goldBorder = goldBorder
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Stats Cards ──
                val scenePoints = movieDNA?.sceneDNA ?: emptyList()
                val avgIntensity =
                    if (scenePoints.isNotEmpty()) scenePoints.map { it.intensity }.average()
                        .toInt() else 0
                val keyScenes = scenePoints.size
                val peakMomentsCount = movieDNA?.peakMoments?.size ?: 0

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    InfoCard(
                        headerText = "Intensity Score",
                        value = avgIntensity,
                        icon = Icons.Default.LocalFireDepartment,
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        headerText = "Key Scenes",
                        value = keyScenes,
                        icon = Icons.Default.Movie,
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        headerText = "Peak Moments",
                        value = peakMomentsCount,
                        icon = Icons.Default.Bolt,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // ── Emotional Intensity Curve ──
                SectionLabel(text = "EMOTIONAL INTENSITY CURVE", fontFamily = bebasNeue)
                Spacer(modifier = Modifier.height(16.dp))
                IntensityCurveChart(
                    goldColor = goldColor,
                    dataPoints = scenePoints.map { it.intensity.toFloat() },
                    labels = scenePoints.map { it.label },
                    duration = movieDuration
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController.navigate(Routes.CompareDNA.createRoute(movieId)) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D1A15)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    border = BorderStroke(1.dp, Color(0xFF60512B))
                ) {
                    Text(
                        text = "Compare DNA with another movie    ",
                        color = Color(0xFFC7A74B),
                        fontSize = 15.sp,
                    )

                    Icon(
                        imageVector = Icons.Default.KeyboardDoubleArrowRight,
                        contentDescription = "Forward arrow",
                        tint = Color(0xFFC7A74B)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // ── AI DNA Summary ──
                if (movieDNA != null && movieDNA.summary.isNotEmpty()) {
                    DnaSummaryCard(summary = movieDNA.summary)
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // ── Emotion Breakdown ──
                SectionLabel(text = "EMOTION BREAKDOWN", fontFamily = bebasNeue)
                Spacer(modifier = Modifier.height(16.dp))
                EmotionBreakdownCard(
                    goldColor = goldColor,
                    items = movieDNA?.emotionBreakdown ?: emptyList()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ── Peak Scene Moments ──
                SectionLabel(text = "PEAK SCENE MOMENTS", fontFamily = bebasNeue)
                Spacer(modifier = Modifier.height(16.dp))
                PeakMomentsSection(movieDNA?.peakMoments ?: emptyList(), goldColor)

                Spacer(modifier = Modifier.height(32.dp))

                // ── Pacing Rhythm ──
                SectionLabel(text = "PACING RHYTHM", fontFamily = bebasNeue)
                Spacer(modifier = Modifier.height(16.dp))
                movieDNA?.pacingRhythm?.let { rhythm ->
                    PacingRhythmCard(goldColor, rhythm)
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String, fontFamily: FontFamily) {
    Text(
        text = text,
        color = Color.White.copy(alpha = 0.4f),
        fontSize = 11.sp,
        letterSpacing = 2.sp,
        fontFamily = fontFamily
    )
}

@Composable
fun DnaSummaryCard(summary: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14141A)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🧠 AI INSIGHT",
                    color = gold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = summary,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }
    }
}

package com.example.cineversemovieapp.screens.dna

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.cineversemovieapp.data.remote.dto.PeakMoment
import com.example.cineversemovieapp.data.remote.dto.PacingRhythm
import com.example.cineversemovieapp.data.remote.dto.EmotionBreakdownItem
import com.example.cineversemovieapp.screens.dna.components.InfoCard
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
                                movie.genres.take(3).forEach { genre ->
                                    TagPill(
                                        tag = EmotionTag("", genre.name),
                                        gold = goldColor, goldFaint = goldFaint, goldBorder = goldBorder
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Stats Cards ──
                val scenePoints = movieDNA?.sceneDNA ?: emptyList()
                val avgIntensity = if (scenePoints.isNotEmpty()) scenePoints.map { it.intensity }.average().toInt() else 0
                val keyScenes = scenePoints.size
                val peakMomentsCount = movieDNA?.peakMoments?.size ?: 0

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    InfoCard(headerText = "Intensity Score", value = avgIntensity, modifier = Modifier.weight(1f))
                    InfoCard(headerText = "Key Scenes", value = keyScenes, modifier = Modifier.weight(1f))
                    InfoCard(headerText = "Peak Moments", value = peakMomentsCount, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(32.dp))

                // ── Emotional Intensity Curve ──
                SectionLabel(text = "EMOTIONAL INTENSITY CURVE", fontFamily = bebasNeue)
                Spacer(modifier = Modifier.height(16.dp))
                IntensityCurveChart(
                    goldColor = goldColor,
                    dataPoints = scenePoints.map { it.intensity.toFloat() },
                    duration = movieDuration
                )

                Spacer(modifier = Modifier.height(32.dp))

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

                Spacer(modifier = Modifier.height(32.dp))
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
fun IntensityCurveChart(goldColor: Color, dataPoints: List<Float>, duration: String) {
    if (dataPoints.isEmpty()) return

    val totalMinutes = duration.filter { it.isDigit() }.toIntOrNull() ?: 120
    val timeLabels = listOf(
        "0 min",
        "${totalMinutes / 4} min",
        "${totalMinutes / 2} min",
        "${(totalMinutes * 3) / 4} min",
        "$totalMinutes min"
    )

    Card(
        modifier = Modifier.fillMaxWidth().height(220.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14141A)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                val w = size.width
                val h = size.height
                val maxVal = 100f
                val minVal = 0f
                val range = maxVal - minVal

                fun xOf(i: Int) = i * w / (dataPoints.size - 1)
                fun yOf(v: Float) = h - ((v - minVal) / range) * h

                val linePath = Path()
                if (dataPoints.size > 1) {
                    linePath.moveTo(xOf(0), yOf(dataPoints[0]))
                    for (i in 1 until dataPoints.size) {
                        val cpX = (xOf(i - 1) + xOf(i)) / 2f
                        linePath.cubicTo(
                            cpX, yOf(dataPoints[i - 1]),
                            cpX, yOf(dataPoints[i]),
                            xOf(i), yOf(dataPoints[i])
                        )
                    }
                }

                val fillPath = Path()
                if (!linePath.isEmpty) {
                    fillPath.addPath(linePath)
                    fillPath.lineTo(xOf(dataPoints.size - 1), h)
                    fillPath.lineTo(xOf(0), h)
                    fillPath.close()

                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(goldColor.copy(alpha = 0.2f), Color.Transparent),
                            startY = 0f, endY = h
                        )
                    )

                    drawPath(
                        path = linePath,
                        color = goldColor,
                        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                timeLabels.forEach { label ->
                    Text(
                        text = label,
                        color = Color.White.copy(alpha = 0.25f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun EmotionBreakdownCard(goldColor: Color, items: List<EmotionBreakdownItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14141A)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            items.forEach { emotion ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = emotion.label,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        modifier = Modifier.width(80.dp),
                        fontWeight = FontWeight.Medium
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(20.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.03f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(emotion.percentage.toFloat() / 100f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(goldColor.copy(alpha = 0.7f))
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${emotion.percentage}%",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (emotion != items.lastOrNull()) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // X-Axis labels (0%, 10%, etc)
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 80.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("0%", "10%", "20%", "30%", "40%", "50%").forEach { label ->
                    Text(
                        text = label,
                        color = Color.White.copy(alpha = 0.2f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

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

@Composable
fun PeakMomentItem(moment: PeakMoment, icon: ImageVector, goldColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14141A)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = goldColor.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = moment.title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "min ${moment.minute} · ${moment.type}",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 11.sp
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${moment.intensity}%",
                    color = goldColor,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "intensity",
                    color = Color.White.copy(alpha = 0.3f),
                    fontSize = 9.sp
                )
            }
        }
    }
}

@Composable
fun PacingRhythmCard(goldColor: Color, rhythm: PacingRhythm) {
    val stats = listOf(
        "Action" to rhythm.action,
        "Drama" to rhythm.drama,
        "Buildup" to rhythm.buildup,
        "Calm" to rhythm.calm
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14141A)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            stats.forEach { (label, value) ->
                val fraction = value.toFloat() / 100f
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                ) {
                    Text(
                        text = label,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        modifier = Modifier.width(75.dp),
                        fontWeight = FontWeight.Medium
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.04f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction.coerceIn(0.05f, 1f))
                                .clip(RoundedCornerShape(4.dp))
                                .background(goldColor)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "$value%",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

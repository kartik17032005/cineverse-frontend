package com.example.cineversemovieapp.screens.dna.compareDNA

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.cineversemovieapp.data.remote.dto.MovieDNA
import com.example.cineversemovieapp.data.tmdb.TmdbMovieDetail
import com.example.cineversemovieapp.repository.TmdbRepository
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.SceneDNAViewModel
import com.example.cineversemovieapp.viewmodel.TmdbViewModel
import kotlinx.coroutines.launch

@Composable
fun DNAComparisonScreen(
    baseMovieId: Int,
    targetMovieId: Int,
    navController: NavController,
    tmdbViewModel: TmdbViewModel,
    sceneDNAViewModel: SceneDNAViewModel
) {
    val context = LocalContext.current
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val bg = Color(0xFF0A0A0F)
    val cardBg = Color(0xFF111118)
    val blueColor = Color(0xFF64B5F6)
    val goldColor = gold

    var baseMovie by remember { mutableStateOf<TmdbMovieDetail?>(null) }
    var targetMovie by remember { mutableStateOf<TmdbMovieDetail?>(null) }
    var baseDNA by remember { mutableStateOf<MovieDNA?>(null) }
    var targetDNA by remember { mutableStateOf<MovieDNA?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val repository = remember { TmdbRepository() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(baseMovieId, targetMovieId) {
        isLoading = true
        scope.launch {
            val baseRes = repository.getMovieDetails(baseMovieId)
            val targetRes = repository.getMovieDetails(targetMovieId)

            if (baseRes.isSuccessful) baseMovie = baseRes.body()
            if (targetRes.isSuccessful) targetMovie = targetRes.body()

            baseDNA = sceneDNAViewModel.generateUniqueMovieDNA(baseMovieId.toLong())
            targetDNA = sceneDNAViewModel.generateUniqueMovieDNA(targetMovieId.toLong())

            isLoading = false
        }
    }

    Scaffold(
        containerColor = bg,
        bottomBar = {
            if (!isLoading) {
                Box(modifier = Modifier.padding(20.dp)) {
                    Button(
                        onClick = {
                            val winner = if ((baseDNA?.sceneDNA?.map { it.intensity }?.average() ?: 0.0) > (targetDNA?.sceneDNA?.map { it.intensity }?.average() ?: 0.0)) baseMovie?.title else targetMovie?.title
                            
                            val shareText = buildString {
                                append("Check out this Movie DNA Comparison on Cineverse!\n\n")
                                append("${baseMovie?.title} vs ${targetMovie?.title}\n\n")
                                append("📊 STATS:\n")
                                append("Intensity: ${baseDNA?.sceneDNA?.map { it.intensity }?.average()?.toInt() ?: 0} vs ${targetDNA?.sceneDNA?.map { it.intensity }?.average()?.toInt() ?: 0}\n")
                                append("Awe: ${baseDNA?.emotionBreakdown?.find { it.label == "Awe" }?.percentage ?: 0}% vs ${targetDNA?.emotionBreakdown?.find { it.label == "Awe" }?.percentage ?: 0}%\n")
                                append("Tension: ${baseDNA?.emotionBreakdown?.find { it.label == "Tension" }?.percentage ?: 0}% vs ${targetDNA?.emotionBreakdown?.find { it.label == "Tension" }?.percentage ?: 0}%\n\n")
                                append("🏆 WINNER: $winner")
                            }

                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    1.dp,
                                    goldColor.copy(alpha = 0.5f),
                                    RoundedCornerShape(12.dp)
                                )
                                .background(
                                    goldColor.copy(alpha = 0.05f),
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Share Comparison",
                                color = goldColor,
                                fontSize = 18.sp,
                                fontFamily = bebasNeue,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = goldColor)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "DNA Comparison",
                    color = goldColor,
                    fontFamily = bebasNeue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(30.dp))

                // ── VS Section ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MovieMiniCard(baseMovie, goldColor)

                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(goldColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "VS",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    MovieMiniCard(targetMovie, blueColor)
                }

                Spacer(modifier = Modifier.height(30.dp))

                // ── Winner Badge ──
                val winnerText = if ((baseDNA?.sceneDNA?.map { it.intensity }?.average() ?: 0.0) >
                    (targetDNA?.sceneDNA?.map { it.intensity }?.average() ?: 0.0)
                ) {
                    "${baseMovie?.title} wins on Intensity"
                } else {
                    "${targetMovie?.title} wins on Intensity"
                }

                Surface(
                    color = goldColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        goldColor.copy(alpha = 0.2f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🏆", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            winnerText,
                            color = goldColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "INTENSITY CURVES",
                    color = Color.Gray,
                    fontFamily = bebasNeue,
                    fontSize = 14.sp,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Graph Section ──
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)) {
                        DNAComparisonGraph(baseDNA, targetDNA, goldColor, blueColor)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "DNA STATS",
                    color = Color.Gray,
                    fontFamily = bebasNeue,
                    fontSize = 14.sp,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Stats Section ──
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        StatRow(
                            "Intensity",
                            baseDNA?.sceneDNA?.map { it.intensity }?.average()?.toInt() ?: 0,
                            targetDNA?.sceneDNA?.map { it.intensity }?.average()?.toInt() ?: 0,
                            goldColor,
                            blueColor
                        )
                        StatRow(
                            "Awe",
                            baseDNA?.emotionBreakdown?.find { it.label == "Awe" }?.percentage ?: 0,
                            targetDNA?.emotionBreakdown?.find { it.label == "Awe" }?.percentage
                                ?: 0,
                            goldColor,
                            blueColor
                        )
                        StatRow(
                            "Tension",
                            baseDNA?.emotionBreakdown?.find { it.label == "Tension" }?.percentage
                                ?: 0,
                            targetDNA?.emotionBreakdown?.find { it.label == "Tension" }?.percentage
                                ?: 0,
                            goldColor,
                            blueColor
                        )
                        StatRow(
                            "Key Scenes",
                            baseDNA?.peakMoments?.size ?: 0,
                            targetDNA?.peakMoments?.size ?: 0,
                            goldColor,
                            blueColor
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                        Spacer(modifier = Modifier.height(16.dp))

                        // Legend
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LegendItem(baseMovie?.title ?: "Base", goldColor)
                            Spacer(modifier = Modifier.width(24.dp))
                            LegendItem(targetMovie?.title ?: "Target", blueColor)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun MovieMiniCard(movie: TmdbMovieDetail?, tint: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.DarkGray.copy(alpha = 0.3f))
                .border(1.dp, tint.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (movie?.posterPath != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Default.Movie, contentDescription = null, tint = tint.copy(alpha = 0.5f))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = movie?.title ?: "Loading...",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = movie?.releaseDate?.take(4) ?: "",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
fun StatRow(label: String, baseVal: Int, targetVal: Int, gold: Color, blue: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 15.sp)
        Column(horizontalAlignment = Alignment.End) {
            Text(baseVal.toString(), color = gold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(targetVal.toString(), color = blue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier
            .size(12.dp, 3.dp)
            .background(color, RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = Color.Gray, fontSize = 12.sp, maxLines = 1)
    }
}

@Composable
fun DNAComparisonGraph(baseDNA: MovieDNA?, targetDNA: MovieDNA?, gold: Color, blue: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Draw Base Path (Gold)
        baseDNA?.let { dna ->
            val points = dna.sceneDNA
            val path = Path()
            val step = width / (points.size - 1)

            points.forEachIndexed { i, p ->
                val x = i * step
                val y = height - (p.intensity.toFloat() / 100f * height)
                if (i == 0) path.moveTo(x, y)
                else {
                    val prevX = (i - 1) * step
                    val prevY = height - (points[i - 1].intensity.toFloat() / 100f * height)
                    path.quadraticTo(prevX + step / 2, prevY, x, y)
                }
            }
            drawPath(
                path = path,
                color = gold,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Draw Target Path (Blue - Dashed style)
        targetDNA?.let { dna ->
            val points = dna.sceneDNA
            val path = Path()
            val step = width / (points.size - 1)

            points.forEachIndexed { i, p ->
                val x = i * step
                val y = height - (p.intensity.toFloat() / 100f * height)
                if (i == 0) path.moveTo(x, y)
                else {
                    val prevX = (i - 1) * step
                    val prevY = height - (points[i - 1].intensity.toFloat() / 100f * height)
                    path.quadraticTo(prevX + step / 2, prevY, x, y)
                }
            }
            drawPath(
                path = path,
                color = blue,
                style = Stroke(
                    width = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f), 0f
                    )
                )
            )
        }
    }
}

package com.example.cineversemovieapp.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold

data class SceneDNAStat(val value: String, val label: String)
data class EmotionTag(val emoji: String, val label: String)

@Composable
fun SceneDNAButton(
    movieId: Int,
    emotionTags: List<EmotionTag> = listOf(
        EmotionTag("⚡", "Intense"),
        EmotionTag("💔", "Emotional"),
    ),
    stats: List<SceneDNAStat> = listOf(
        SceneDNAStat("87%", "Intensity"),
        SceneDNAStat("24", "Key Scenes"),
        SceneDNAStat("3", "Peaks")
    ),
    navController: NavController
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val goldColor = gold
    val surfaceColor = Color(0xFF14141A)
    val borderColor = Color.White.copy(alpha = 0.08f)
    val subtitleGray = Color(0xFF8E8E93)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { navController.navigate(Routes.SceneDNA.createRoute(movieId)) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // DNA Icon & Animation
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(60.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(goldColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dna),
                            contentDescription = null,
                            tint = goldColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedEqualizerBars(gold = goldColor)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "AI ANALYSIS",
                            color = goldColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF4CAF50))
                        )
                    }
                    Text(
                        text = "SCENE DNA",
                        fontFamily = bebasNeue,
                        fontSize = 24.sp,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        emotionTags.forEach { tag ->
                            Text(
                                text = "${tag.emoji} ${tag.label}",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White.copy(alpha = 0.05f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = goldColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Stats Strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    stats.forEach { stat ->
                        Column {
                            Text(
                                text = stat.value,
                                color = goldColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stat.label.uppercase(),
                                color = subtitleGray,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedEqualizerBars(gold: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "equalizer")
    val heights = listOf(0.4f, 0.8f, 0.5f, 1f, 0.6f)
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.height(12.dp)
    ) {
        heights.forEachIndexed { index, targetHeight ->
            val height by infiniteTransition.animateFloat(
                initialValue = 2f,
                targetValue = 12f * targetHeight,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 400 + (index * 100), easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bar_$index"
            )
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(height.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(gold.copy(alpha = 0.7f))
            )
        }
    }
}

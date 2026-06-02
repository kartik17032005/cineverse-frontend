package com.example.cineversemovieapp.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun SplashScreen(navController: NavController) {
    val goldColor = gold
    val bg = Color(0xFF0A0A0F)
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    val infiniteTransition = rememberInfiniteTransition(label = "splash_assets")

    // Rotation for different orbits
    val orbit1Rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit1"
    )

    val orbit2Rotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit2"
    )

    val orbit3Rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit3"
    )

    val playButtonPulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(Unit) {
        delay(4000)
        navController.navigate(Routes.Login.route) {
            popUpTo(Routes.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        // Subtle Star Background
        StarBackground()

        // UI Corner Accents
        CornerMarkers(goldColor)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ── Revolving Graphic ──
            Box(
                modifier = Modifier.size(300.dp),
                contentAlignment = Alignment.Center
            ) {
                // Static orbits (faint full circles)
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val radii = listOf(55.dp, 80.dp, 105.dp, 130.dp)
                    radii.forEach { r ->
                        drawCircle(
                            color = goldColor.copy(alpha = 0.05f),
                            radius = r.toPx(),
                            style = Stroke(width = 1.dp.toPx())
                        )
                    }
                }

                // Orbit 1 Segment (Inner)
                Canvas(modifier = Modifier.fillMaxSize().rotate(orbit1Rotation)) {
                    drawArc(
                        color = goldColor.copy(alpha = 0.3f),
                        startAngle = 0f,
                        sweepAngle = 100f,
                        useCenter = false,
                        topLeft = Offset(center.x - 55.dp.toPx(), center.y - 55.dp.toPx()),
                        size = Size(110.dp.toPx(), 110.dp.toPx()),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }

                // Orbit 2 with Dot
                Box(modifier = Modifier.fillMaxSize().rotate(orbit2Rotation)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Segment
                        drawArc(
                            color = goldColor.copy(alpha = 0.5f),
                            startAngle = 180f,
                            sweepAngle = 120f,
                            useCenter = false,
                            topLeft = Offset(center.x - 80.dp.toPx(), center.y - 80.dp.toPx()),
                            size = Size(160.dp.toPx(), 160.dp.toPx()),
                            style = Stroke(width = 2.dp.toPx())
                        )
                        
                        // The Dot
                        drawCircle(
                            color = goldColor,
                            radius = 4.5.dp.toPx(),
                            center = Offset(center.x + 80.dp.toPx(), center.y)
                        )
                    }
                }

                // Orbit 3 Segment
                Canvas(modifier = Modifier.fillMaxSize().rotate(orbit3Rotation)) {
                    drawArc(
                        color = goldColor.copy(alpha = 0.2f),
                        startAngle = 270f,
                        sweepAngle = 150f,
                        useCenter = false,
                        topLeft = Offset(center.x - 105.dp.toPx(), center.y - 105.dp.toPx()),
                        size = Size(210.dp.toPx(), 210.dp.toPx()),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }

                // Central Play Button
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .scale(playButtonPulse)
                        .background(goldColor.copy(alpha = 0.05f), CircleShape)
                        .border(1.dp, goldColor.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = goldColor,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── App Name ──
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "CINE",
                    color = Color.White,
                    fontFamily = bebasNeue,
                    fontSize = 60.sp,
                    letterSpacing = 4.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "VERSE",
                    color = goldColor,
                    fontFamily = bebasNeue,
                    fontSize = 60.sp,
                    letterSpacing = 4.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // ── Tagline with separator lines ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Box(modifier = Modifier.width(50.dp).height(0.5.dp).background(goldColor.copy(alpha = 0.4f)))
                Text(
                    text = "  ENTER A NEW WORLD  ",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    letterSpacing = 5.sp,
                    fontWeight = FontWeight.Normal
                )
                Box(modifier = Modifier.width(50.dp).height(0.5.dp).background(goldColor.copy(alpha = 0.4f)))
            }
        }

        // ── Bottom Arrow ──
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
                .size(44.dp)
                .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun StarBackground() {
    val stars = remember {
        List(100) {
            Offset(Random.nextFloat(), Random.nextFloat()) to Random.nextFloat()
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        stars.forEach { (pos, alpha) ->
            drawCircle(
                color = Color.White.copy(alpha = alpha * 0.4f),
                radius = 1.dp.toPx(),
                center = Offset(pos.x * size.width, pos.y * size.height)
            )
        }
    }
}

@Composable
fun CornerMarkers(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val stroke = 1.5.dp.toPx()
        val len = 20.dp.toPx()
        val pad = 24.dp.toPx()

        // Top Left
        drawLine(color.copy(0.3f), Offset(pad, pad), Offset(pad + len, pad), stroke)
        drawLine(color.copy(0.3f), Offset(pad, pad), Offset(pad, pad + len), stroke)

        // Bottom Right
        drawLine(color.copy(0.3f), Offset(size.width - pad, size.height - pad), Offset(size.width - pad - len, size.height - pad), stroke)
        drawLine(color.copy(0.3f), Offset(size.width - pad, size.height - pad), Offset(size.width - pad, size.height - pad - len), stroke)
    }
}

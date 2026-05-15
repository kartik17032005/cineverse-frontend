package com.example.cineversemovieapp.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.utils.MoodDetector
import com.example.cineversemovieapp.viewmodel.TmdbViewModel
import kotlinx.coroutines.delay

@Composable
fun MoodScreen(
    navController: NavController,
    tmdbViewModel: TmdbViewModel = viewModel()
) {
    val gold = gold
    val bg = Color(0xFF0A0A0F)
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // ── States ──
    // tracks if user tapped scan button
    var isScanning by remember { mutableStateOf(false) }

    // stores the detected mood
    var detectedMood by remember { mutableStateOf("") }

    // true when we have a result
    var scanComplete by remember { mutableStateOf(false) }

    // tracks camera permission
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // ── Permission launcher ──
    // this shows the system permission dialog
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            isScanning = true  // start scanning after permission granted
        }
    }

    // ── Pulsing animation for scan button ──
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // ── Helper functions ──
    fun getMoodEmoji(mood: String) = when (mood) {
        "HAPPY"   -> "😄"
        "SAD"     -> "😢"
        "SLEEPY"  -> "😴"
        "NEUTRAL" -> "😐"
        else      -> "🎭"
    }

    fun getMoodMessage(mood: String) = when (mood) {
        "HAPPY"   -> "You look happy! Here are some comedies 🎉"
        "SAD"     -> "Feeling down? Let's watch something emotional 🎭"
        "SLEEPY"  -> "Looks like you need an adventure to wake up! 🚀"
        "NEUTRAL" -> "Chill mood? Here's some action for you 💥"
        else      -> "Finding movies for you..."
    }

    fun getMoodGenreId(mood: String) = when (mood) {
        "HAPPY"   -> 35   // Comedy
        "SAD"     -> 18   // Drama
        "SLEEPY"  -> 12   // Adventure
        "NEUTRAL" -> 28   // Action
        else      -> 35
    }

    // ── Auto navigate after scan complete ──
    // waits 2.5 seconds so user can see the result
    // then fetches movies and goes to home
    LaunchedEffect(scanComplete) {
        if (scanComplete && detectedMood.isNotEmpty()) {
            delay(2500)
            
            // Fetch mood-based reels first
            tmdbViewModel.getReelsMovies(detectedMood)
            
            // Also fetch for home screen by genre
            tmdbViewModel.searchMoviesByGenre(getMoodGenreId(detectedMood))
            
            // Navigate to Reels screen instead of Home
            navController.navigate(Routes.Reels.route) {
                popUpTo(Routes.Mood.route) { inclusive = true }
            }
        }
    }

    // ── UI ──
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {

            // ── Title ──
            Text(
                text = "MOOD",
                fontFamily = bebasNeue,
                fontSize = 40.sp,
                letterSpacing = 6.sp,
                color = Color.White
            )
            Text(
                text = "DETECTOR",
                fontFamily = bebasNeue,
                fontSize = 40.sp,
                letterSpacing = 6.sp,
                color = gold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Let us find the perfect movie for your mood",
                fontSize = 13.sp,
                color = Color(0xFF5E5C68),
                fontWeight = FontWeight.Light
            )

            Spacer(Modifier.height(40.dp))

            // ── Camera circle ──
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .scale(if (isScanning && !scanComplete) pulse else 1f)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (scanComplete) gold
                        else if (isScanning) gold.copy(alpha = 0.5f)
                        else Color.White.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    // ── Scan complete — show emoji result ──
                    scanComplete -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            gold.copy(alpha = 0.15f),
                                            bg
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = getMoodEmoji(detectedMood),
                                    fontSize = 72.sp
                                )
                                Text(
                                    text = detectedMood,
                                    fontFamily = bebasNeue,
                                    fontSize = 28.sp,
                                    letterSpacing = 4.sp,
                                    color = gold
                                )
                            }
                        }
                    }

                    // ── Scanning — show live camera ──
                    isScanning && hasCameraPermission -> {
                        AndroidView(
                            factory = { ctx ->
                                // create PreviewView — this shows camera feed
                                PreviewView(ctx).apply {
                                    implementationMode =
                                        PreviewView.ImplementationMode.COMPATIBLE
                                    scaleType = PreviewView.ScaleType.FILL_CENTER
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            update = { previewView ->
                                // start mood detection
                                val detector = MoodDetector(
                                    context = previewView.context,
                                    onMoodDetected = { mood ->
                                        // only accept first result
                                        if (!scanComplete) {
                                            detectedMood = mood
                                            scanComplete = true
                                            isScanning = false
                                        }
                                    }
                                )
                                detector.startDetection(lifecycleOwner, previewView)
                            }
                        )

                        // scanning label on top of camera
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Transparent),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "● Scanning...",
                                    fontSize = 12.sp,
                                    color = gold
                                )
                            }
                        }
                    }

                    // ── Default — show emoji placeholder ──
                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF1A1A24)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎭", fontSize = 72.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            // ── Bottom section ──
            when {

                // ── Scan complete — show message ──
                scanComplete -> {
                    Text(
                        text = getMoodMessage(detectedMood),
                        fontSize = 14.sp,
                        color = Color(0xFF5E5C68),
                        fontWeight = FontWeight.Light
                    )
                }

                // ── Not yet scanning — show button ──
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(gold, Color(0xFFa87830))
                                )
                            )
                            .clickable {
                                // check permission first
                                if (hasCameraPermission) {
                                    isScanning = true
                                } else {
                                    // ask for permission
                                    permissionLauncher.launch(
                                        Manifest.permission.CAMERA
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isScanning) "SCANNING..." else "SCAN MY MOOD  🎭",
                            fontFamily = bebasNeue,
                            fontSize = 18.sp,
                            letterSpacing = 2.sp,
                            color = bg
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Skip button
                    Text(
                        text = "Skip for now →",
                        fontSize = 13.sp,
                        color = Color(0xFF5E5C68),
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.Home.route) {
                                popUpTo(Routes.Mood.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

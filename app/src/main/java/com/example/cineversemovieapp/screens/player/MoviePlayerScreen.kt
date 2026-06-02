package com.example.cineversemovieapp.screens.player

/**
 * ═══════════════════════════════════════════════════════════════
 * MoviePlayerScreen.kt — Cineverse Movie Player (Fixed & Optimized)
 * ═══════════════════════════════════════════════════════════════
 *
 * UPDATES:
 * 1. Removed @Preview from MoviePlayerScreen to prevent ExoPlayer crashes.
 * 2. Fixed AnimatedVisibility ambiguity with explicit package naming.
 * 3. Ensured PlayerView uses MATCH_PARENT LayoutParams for visibility.
 * 4. All controls layered correctly in a Box to prevent gesture blocking.
 * ═══════════════════════════════════════════════════════════════
 */

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.cineversemovieapp.R
import kotlinx.coroutines.delay

// ── Colors ────────────────────────────────────────────────────
private val Gold     = Color(0xFFCCA246)
private val Gold2    = Color(0xFFA87830)
private val Bg       = Color(0xFF0A0A0F)
private val Surface2 = Color(0xFF191927)
private val Surface3 = Color(0xFF13131E)
private val TextCol  = Color(0xFFF0EDE8)
private val Muted    = Color(0xFF5E5C68)
private val Border   = Color.White.copy(alpha = 0.07f)

// ─────────────────────────────────────────────────────────────
// MAIN PLAYER SCREEN
// ─────────────────────────────────────────────────────────────
@OptIn(UnstableApi::class)
@Composable
fun MoviePlayerScreen(
    movieTitle: String = "The Dark Knight",
    movieYear: String = "2008",
    movieGenre: String = "Action • Drama",
    movieRating: String = "9.0",
    videoUrl: String = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    onBack: () -> Unit = {}
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val context   = LocalContext.current
    val activity  = context as? Activity

    // ── Build ExoPlayer once ──────────────────────────────────
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
        }
    }

    // ── Reactive state ────────────────────────────────────────
    var isPlaying       by remember { mutableStateOf(false) }
    var isBuffering     by remember { mutableStateOf(true) }
    var isEnded         by remember { mutableStateOf(false) }
    var showControls    by remember { mutableStateOf(true) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var totalDuration   by remember { mutableLongStateOf(0L) }
    var isFullscreen    by remember { mutableStateOf(false) }
    var isMuted         by remember { mutableStateOf(false) }
    var selectedQuality by remember { mutableStateOf("HD") }
    var subsOn          by remember { mutableStateOf(false) }

    // ── Player event listener ─────────────────────────────────
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }
            override fun onPlaybackStateChanged(state: Int) {
                isBuffering = (state == Player.STATE_BUFFERING)
                isEnded     = (state == Player.STATE_ENDED)
                if (state == Player.STATE_READY) {
                    totalDuration = exoPlayer.duration.coerceAtLeast(0L)
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose { exoPlayer.removeListener(listener) }
    }

    // ── Auto-hide controls after 4 seconds ───────────────────
    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying) {
            delay(4000)
            showControls = false
        }
    }

    // ── Seek bar polling every 500ms ──────────────────────────
    LaunchedEffect(exoPlayer) {
        while (true) {
            currentPosition = exoPlayer.currentPosition
            delay(500)
        }
    }

    // ── Orientation control ───────────────────────────────────
    LaunchedEffect(isFullscreen) {
        activity?.requestedOrientation = if (isFullscreen)
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        else
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    // ── Cleanup when leaving the screen ──────────────────────
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
            activity?.requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    // ── Root layout ───────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ══════════════════════════════════════════════════
            // VIDEO PLAYER AREA
            // ══════════════════════════════════════════════════
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isFullscreen) Modifier.fillMaxHeight()
                        else Modifier.aspectRatio(16f / 9f)
                    )
                    .background(Color.Black)
            ) {
                // ── LAYER 1: ExoPlayer Surface ────────────────
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            // CRITICAL: Ensure PlayerView fills parent Box
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            player = exoPlayer
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // ── LAYER 2: Gesture Layer ────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { showControls = !showControls },
                                onDoubleTap = { offset ->
                                    if (offset.x < size.width / 2) {
                                        exoPlayer.seekTo((exoPlayer.currentPosition - 10_000L).coerceAtLeast(0L))
                                    } else {
                                        exoPlayer.seekTo(exoPlayer.currentPosition + 10_000L)
                                    }
                                    showControls = true
                                }
                            )
                        }
                )

                // ── LAYER 3: Controls Overlay ─────────────────
                // FIX: Explicitly use package name to avoid ColumnScope ambiguity from the parent Column
                androidx.compose.animation.AnimatedVisibility(
                    visible = showControls,
                    enter = fadeIn(tween(300)),
                    exit  = fadeOut(tween(300))
                ) {
                    PlayerControlsOverlay(
                        bebasNeue       = bebasNeue,
                        movieTitle      = movieTitle,
                        movieYear       = movieYear,
                        isPlaying       = isPlaying,
                        isBuffering     = isBuffering,
                        isEnded         = isEnded,
                        currentPosition = currentPosition,
                        totalDuration   = totalDuration,
                        isFullscreen    = isFullscreen,
                        isMuted         = isMuted,
                        onBack          = onBack,
                        onPlayPause     = {
                            when {
                                isEnded -> {
                                    exoPlayer.seekTo(0L)
                                    exoPlayer.play()
                                }
                                exoPlayer.isPlaying -> exoPlayer.pause()
                                else -> exoPlayer.play()
                            }
                        },
                        onSeek = { ms -> exoPlayer.seekTo(ms) },
                        onRewind = {
                            exoPlayer.seekTo((exoPlayer.currentPosition - 10_000L).coerceAtLeast(0L))
                        },
                        onFastForward = {
                            exoPlayer.seekTo(exoPlayer.currentPosition + 10_000L)
                        },
                        onFullscreen = { isFullscreen = !isFullscreen },
                        onMute = {
                            isMuted = !isMuted
                            exoPlayer.volume = if (isMuted) 0f else 1f
                        }
                    )
                }
            } // End video Box

            // ══════════════════════════════════════════════════
            // DETAILS PANEL (Portrait only)
            // ══════════════════════════════════════════════════
            if (!isFullscreen) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 22.dp)
                ) {
                    // Title and Actions
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = movieTitle.uppercase(),
                                fontFamily = bebasNeue,
                                fontSize = 30.sp,
                                letterSpacing = 2.sp,
                                color = TextCol,
                                lineHeight = 34.sp
                            )
                            Spacer(Modifier.height(6.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(movieYear, fontSize = 13.sp, color = Muted)
                                Box(Modifier.size(3.dp).background(Muted, CircleShape))
                                Text(movieGenre, fontSize = 13.sp, color = Muted)
                            }
                            Spacer(Modifier.height(10.dp))
                            // IMDb Rating
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .background(Gold.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Icon(Icons.Outlined.Star, null, tint = Gold, modifier = Modifier.size(13.dp))
                                Text(movieRating, fontSize = 12.sp, color = Gold, fontWeight = FontWeight.Bold)
                                Text("IMDb", fontSize = 10.sp, color = Gold.copy(alpha = 0.7f))
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ActionIconBtn(icon = Icons.Outlined.Share)
                            ActionIconBtn(icon = Icons.Outlined.BookmarkBorder)
                        }
                    }

                    Spacer(Modifier.height(28.dp))
                    HorizontalDivider(color = Border)
                    Spacer(Modifier.height(28.dp))

                    // Quality Selection
                    SectionLabel(text = "QUALITY", fontFamily = bebasNeue)
                    Spacer(Modifier.height(14.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("AUTO", "HD", "FHD", "4K").forEach { quality ->
                            QualityChip(
                                label = quality,
                                isSelected = selectedQuality == quality,
                                onClick = { selectedQuality = quality },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(Modifier.height(28.dp))
                    HorizontalDivider(color = Border)
                    Spacer(Modifier.height(28.dp))

                    // Subtitles
                    SectionLabel(text = "SUBTITLES", fontFamily = bebasNeue)
                    Spacer(Modifier.height(14.dp))
                    SubtitleRow(isOn = subsOn, onToggle = { subsOn = it })

                    Spacer(Modifier.height(40.dp))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// PLAYER CONTROLS OVERLAY
// ─────────────────────────────────────────────────────────────
@Composable
fun PlayerControlsOverlay(
    bebasNeue: FontFamily,
    movieTitle: String,
    movieYear: String,
    isPlaying: Boolean,
    isBuffering: Boolean,
    isEnded: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    isFullscreen: Boolean,
    isMuted: Boolean,
    onBack: () -> Unit,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onRewind: () -> Unit,
    onFastForward: () -> Unit,
    onFullscreen: () -> Unit,
    onMute: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black.copy(alpha = 0.75f),
                        Color.Transparent,
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.85f)
                    )
                )
            )
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.TopCenter)
        ) {
            PlayerIconBtn(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Back", tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(movieTitle.uppercase(), fontFamily = bebasNeue, fontSize = 17.sp, letterSpacing = 2.sp, color = Color.White)
                Text(movieYear, fontSize = 12.sp, color = Color.White.copy(alpha = 0.55f))
            }
            PlayerIconBtn(onClick = onMute) {
                Icon(
                    if (isMuted) Icons.AutoMirrored.Outlined.VolumeOff else Icons.AutoMirrored.Outlined.VolumeUp,
                    if (isMuted) "Unmute" else "Mute",
                    tint = if (isMuted) Gold else Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Center Controls
        Box(contentAlignment = Alignment.Center, modifier = Modifier.align(Alignment.Center)) {
            if (isBuffering) {
                CircularProgressIndicator(color = Gold, strokeWidth = 3.dp, modifier = Modifier.size(72.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(36.dp)) {
                PlayerIconBtn(onClick = onRewind, size = 52.dp, bgAlpha = 0.3f) {
                    Icon(Icons.Outlined.Replay10, "Rewind", tint = Color.White, modifier = Modifier.size(30.dp))
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(72.dp)
                        .background(Brush.linearGradient(listOf(Gold, Gold2)), CircleShape)
                        .clickable { onPlayPause() }
                ) {
                    Icon(
                        when {
                            isEnded -> Icons.Outlined.Replay
                            isPlaying -> Icons.Outlined.Pause
                            else -> Icons.Outlined.PlayArrow
                        },
                        "Play/Pause", tint = Bg, modifier = Modifier.size(38.dp)
                    )
                }
                PlayerIconBtn(onClick = onFastForward, size = 52.dp, bgAlpha = 0.3f) {
                    Icon(Icons.Outlined.Forward10, "Forward", tint = Color.White, modifier = Modifier.size(30.dp))
                }
            }
        }

        // Bottom Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
                Text(formatTime(currentPosition), fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(formatTime(totalDuration), fontSize = 12.sp, color = Color.White.copy(alpha = 0.5f))
            }
            Slider(
                value = if (totalDuration > 0) (currentPosition.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f) else 0f,
                onValueChange = { fraction -> onSeek((fraction * totalDuration).toLong()) },
                colors = SliderDefaults.colors(thumbColor = Gold, activeTrackColor = Gold, inactiveTrackColor = Color.White.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                PlayerIconBtn(onClick = onFullscreen, bgAlpha = 0.3f) {
                    Icon(if (isFullscreen) Icons.Outlined.FullscreenExit else Icons.Outlined.Fullscreen, "Fullscreen", tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// REUSABLE SMALL COMPOSABLES
// ─────────────────────────────────────────────────────────────

@Composable
fun PlayerIconBtn(onClick: () -> Unit, size: androidx.compose.ui.unit.Dp = 40.dp, bgAlpha: Float = 0.4f, content: @Composable () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size).background(Color.Black.copy(alpha = bgAlpha), CircleShape).clickable { onClick() }) { content() }
}

@Composable
fun ActionIconBtn(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(46.dp).background(Surface2, CircleShape).border(1.dp, Border, CircleShape).clickable { }) {
        Icon(icon, null, tint = TextCol, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun SectionLabel(text: String, fontFamily: FontFamily) {
    Text(text = text, fontFamily = fontFamily, fontSize = 13.sp, letterSpacing = 2.5.sp, color = Muted)
}

@Composable
fun QualityChip(label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Gold.copy(alpha = 0.14f) else Surface2)
            .border(1.dp, if (isSelected) Gold.copy(alpha = 0.5f) else Border, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 13.dp)
    ) {
        Text(text = label, fontSize = 13.sp, color = if (isSelected) Gold else Muted, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
fun SubtitleRow(isOn: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().background(Surface2, RoundedCornerShape(14.dp)).border(1.dp, Border, RoundedCornerShape(14.dp)).padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(40.dp).background(Gold.copy(alpha = 0.1f), RoundedCornerShape(10.dp))) {
                Icon(Icons.Outlined.Subtitles, null, tint = Gold, modifier = Modifier.size(20.dp))
            }
            Column {
                Text("Subtitles", fontSize = 15.sp, color = TextCol, fontWeight = FontWeight.Medium)
                Text("Auto detect language", fontSize = 12.sp, color = Muted)
            }
        }
        Switch(checked = isOn, onCheckedChange = onToggle, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Gold, uncheckedThumbColor = Muted, uncheckedTrackColor = Surface3))
    }
}

fun formatTime(ms: Long): String {
    if (ms <= 0L) return "00:00"
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) "%d:%02d:%02d".format(hours, minutes, seconds) else "%02d:%02d".format(minutes, seconds)
}

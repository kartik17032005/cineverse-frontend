package com.example.cineversemovieapp.screens.badge

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.data.badge.CineBadge
import com.example.cineversemovieapp.enums.BadgeTier
import com.example.cineversemovieapp.viewmodel.BadgeViewModel
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

// ── Colors ──────────────────────────────────────────────
private val Gold = Color(0xFFCCA246)
private val Bg = Color(0xFF0A0A0F)
private val Surface2 = Color(0xFF191927)
private val TextColor = Color(0xFFF0EDE8)
private val Muted = Color(0xFF5E5C68)
private val Border = Color.White.copy(alpha = 0.07f)
private val Bronze = Color(0xFFCD7F32)
private val Silver = Color(0xFFA8A9AD)

@Composable
fun CineBadgesScreen(
    onBack: () -> Unit = {},
    viewModel: BadgeViewModel = viewModel()
) {
    val badges by viewModel.badges.collectAsState()
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    var showToast by remember { mutableStateOf(false) }
    var toastBadge by remember { mutableStateOf<CineBadge?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 40.dp)
        ) {
            // ── Header ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, top = 52.dp, bottom = 20.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Surface2, CircleShape)
                        .border(0.5.dp, Border, CircleShape)
                        .clickable { onBack() }
                ) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = TextColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "CINE",
                        fontFamily = bebasNeue,
                        fontSize = 22.sp,
                        letterSpacing = 4.sp,
                        color = TextColor
                    )
                    Text(
                        "BADGES",
                        fontFamily = bebasNeue,
                        fontSize = 22.sp,
                        letterSpacing = 4.sp,
                        color = Gold,
                        modifier = Modifier.offset(y = (-6).dp)
                    )
                }

                Box(modifier = Modifier.size(38.dp))
            }

            // ── Stats ──
            BadgeStatsRow(
                badges = badges,
                modifier = Modifier.padding(horizontal = 18.dp)
            )

            Spacer(Modifier.height(24.dp))

            // ── Toast ──
            if (showToast && toastBadge != null) {
                Box(modifier = Modifier.padding(horizontal = 18.dp)) {
                    BadgeUnlockToast(badge = toastBadge!!) { showToast = false }
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── Section: Unlocked ──
            val unlockedBadges = badges.filter { it.isUnlocked }
            if (unlockedBadges.isNotEmpty()) {
                Text(
                    text = "UNLOCKED",
                    fontFamily = bebasNeue,
                    fontSize = 13.sp,
                    letterSpacing = 2.5.sp,
                    color = Muted,
                    modifier = Modifier.padding(start = 18.dp, bottom = 12.dp)
                )

                BadgeGrid(
                    badges = unlockedBadges,
                    onBadgeClick = {
                        toastBadge = it
                        showToast = true
                    }
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Section: In Progress ──
            val inProgressBadges = badges.filter { !it.isUnlocked }
            if (inProgressBadges.isNotEmpty()) {
                Text(
                    text = "IN PROGRESS",
                    fontFamily = bebasNeue,
                    fontSize = 13.sp,
                    letterSpacing = 2.5.sp,
                    color = Muted,
                    modifier = Modifier.padding(start = 18.dp, bottom = 12.dp)
                )

                BadgeGrid(badges = inProgressBadges)
            }
        }
    }
}

@Composable
fun BadgeGrid(
    badges: List<CineBadge>,
    onBadgeClick: (CineBadge) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 2000.dp),
        userScrollEnabled = false
    ) {
        items(badges) { badge ->
            CineBadgeItem(badge = badge, onClick = onBadgeClick)
        }
    }
}

@Composable
fun CineBadgeItem(
    badge: CineBadge,
    modifier: Modifier = Modifier,
    onClick: (CineBadge) -> Unit = {}
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val isLocked = !badge.isUnlocked && badge.progress == 0
    val progress = badge.progress.toFloat() / badge.goal.toFloat()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Surface2, RoundedCornerShape(16.dp))
            .border(0.5.dp, Border, RoundedCornerShape(16.dp))
            .clickable { onClick(badge) }
            .padding(14.dp)
            .alpha(if (isLocked) 0.45f else 1f)
    ) {
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.size(72.dp)) {
            BadgeIcon(
                badge = badge,
                pulseAlpha = if (badge.isUnlocked && badge.tier == BadgeTier.GOLD) pulseAlpha else 0.3f,
                modifier = Modifier.size(72.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = when {
                            isLocked -> Muted
                            badge.tier == BadgeTier.GOLD -> Gold
                            badge.tier == BadgeTier.SILVER -> Silver
                            else -> Bronze
                        },
                        shape = CircleShape
                    )
                    .border(2.dp, Bg, CircleShape)
            ) {
                Text(
                    text = when {
                        isLocked -> "?"
                        badge.tier == BadgeTier.GOLD -> "G"
                        badge.tier == BadgeTier.SILVER -> "S"
                        else -> "B"
                    },
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (badge.tier == BadgeTier.GOLD && !isLocked) Bg else Color.White
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = badge.name,
            fontFamily = bebasNeue,
            fontSize = 13.sp,
            color = if (isLocked) Muted else TextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = badge.description,
            fontSize = 10.sp,
            color = Muted,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(Border, RoundedCornerShape(100.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(3.dp)
                    .background(
                        color = when {
                            isLocked -> Muted
                            badge.tier == BadgeTier.GOLD -> Gold
                            badge.tier == BadgeTier.SILVER -> Silver
                            else -> Bronze
                        },
                        shape = RoundedCornerShape(100.dp)
                    )
            )
        }

        Spacer(Modifier.height(5.dp))

        Text(text = "${badge.progress} / ${badge.goal}", fontSize = 10.sp, color = Muted)
    }
}

@Composable
fun BadgeStatsRow(badges: List<CineBadge>, modifier: Modifier = Modifier) {
    val unlocked = badges.count { it.isUnlocked }
    val gold = badges.count { it.isUnlocked && it.tier == BadgeTier.GOLD }
    val inProgress = badges.count { !it.isUnlocked && it.progress > 0 }

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        StatItem("$unlocked", "Unlocked", Gold, Modifier.weight(1f))
        StatItem("$gold", "Gold", Gold, Modifier.weight(1f))
        StatItem("$inProgress", "In Progress", Color(0xFF8C5AC8), Modifier.weight(1f))
    }
}

@Composable
fun StatItem(value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Surface2, RoundedCornerShape(12.dp))
            .border(0.5.dp, Border, RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = value,
            fontFamily = FontFamily(Font(R.font.bebas_neue_regular)),
            fontSize = 26.sp,
            color = color,
            letterSpacing = 1.sp
        )
        Text(text = label, fontSize = 11.sp, color = Muted)
    }
}

@Composable
fun BadgeIcon(
    badge: CineBadge,
    pulseAlpha: Float,
    modifier: Modifier = Modifier
) {
    val isLocked = !badge.isUnlocked && badge.progress == 0
    val ringColor = if (isLocked) Muted else badge.ringColor

    Canvas(modifier = modifier) {
        val cx = size.width / 2
        val cy = size.height / 2
        val outerR = size.width / 2 - 2f
        val innerR = outerR * 0.72f

        if (!isLocked) {
            drawCircle(
                color = ringColor.copy(alpha = pulseAlpha * 0.15f),
                radius = outerR + 4f,
                center = Offset(cx, cy)
            )
        }

        drawCircle(
            color = Color(0xFF1A1A24),
            radius = outerR,
            center = Offset(cx, cy)
        )

        drawCircle(
            color = ringColor,
            radius = outerR,
            center = Offset(cx, cy),
            style = Stroke(width = 2.5f)
        )

        drawCircle(
            color = Color(0xFF111118),
            radius = innerR,
            center = Offset(cx, cy)
        )

        drawCircle(
            color = ringColor.copy(alpha = 0.2f),
            radius = innerR,
            center = Offset(cx, cy),
            style = Stroke(width = 1f)
        )

        if (isLocked) {
            val lw = size.width * 0.28f
            val lh = size.height * 0.22f
            val lx = cx - lw / 2
            val ly = cy - lh * 0.3f
            drawRoundRect(
                color = Muted,
                topLeft = Offset(lx, ly),
                size = Size(lw, lh),
                cornerRadius = CornerRadius(4f),
                style = Stroke(width = 2.5f)
            )
            drawArc(
                color = Muted,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(lx + lw * 0.2f, ly - lh * 0.7f),
                size = Size(lw * 0.6f, lh * 0.7f),
                style = Stroke(width = 2.5f)
            )
            return@Canvas
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        BadgeSymbolCanvas(badge.ringColor, badge.type.name.lowercase(), modifier)
    }
}

@Composable
fun BadgeSymbolCanvas(
    color: Color,
    type: String,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2
        val cy = size.height / 2
        val s = size.width * 0.22f

        when (type) {
            "star" -> {
                val path = Path().apply {
                    val points = 5
                    val outerR = s * 1.1f
                    val innerR = s * 0.45f
                    for (i in 0 until points * 2) {
                        val angle = (Math.PI * i / points - Math.PI / 2).toFloat()
                        val r = if (i % 2 == 0) outerR else innerR
                        val x = cx + r * cos(angle.toDouble()).toFloat()
                        val y = cy + r * sin(angle.toDouble()).toFloat()
                        if (i == 0) moveTo(x, y) else lineTo(x, y)
                    }
                    close()
                }
                drawPath(path, color = color)
            }

            "heart" -> {
                val path = Path().apply {
                    moveTo(cx, cy + s * 0.8f)
                    cubicTo(
                        cx - s * 1.5f,
                        cy + s * 0.1f,
                        cx - s * 1.5f,
                        cy - s * 0.8f,
                        cx,
                        cy - s * 0.2f
                    )
                    cubicTo(
                        cx + s * 1.5f,
                        cy - s * 0.8f,
                        cx + s * 1.5f,
                        cy + s * 0.1f,
                        cx,
                        cy + s * 0.8f
                    )
                    close()
                }
                drawPath(path, color = color)
            }

            "play" -> {
                val path = Path().apply {
                    moveTo(cx - s * 0.6f, cy - s * 1.0f)
                    lineTo(cx - s * 0.6f, cy + s * 1.0f)
                    lineTo(cx + s * 1.1f, cy)
                    close()
                }
                drawPath(path, color = color)
            }

            "clock" -> {
                drawCircle(
                    color = color,
                    radius = s,
                    center = Offset(cx, cy),
                    style = Stroke(width = 2.5f)
                )
                drawLine(
                    color = color,
                    start = Offset(cx, cy),
                    end = Offset(cx + s * 0.6f, cy - s * 0.5f),
                    strokeWidth = 2.5f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = color,
                    start = Offset(cx, cy),
                    end = Offset(cx, cy - s * 0.8f),
                    strokeWidth = 2.5f,
                    cap = StrokeCap.Round
                )
                drawCircle(color = color, radius = 3f, center = Offset(cx, cy))
            }

            "globe" -> {
                drawCircle(
                    color = color,
                    radius = s,
                    center = Offset(cx, cy),
                    style = Stroke(width = 2.5f)
                )
                drawLine(
                    color = color.copy(alpha = 0.5f),
                    start = Offset(cx - s, cy),
                    end = Offset(cx + s, cy),
                    strokeWidth = 1.5f
                )
                drawArc(
                    color = color,
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx - s * 0.5f, cy - s),
                    size = Size(s, s * 2f),
                    style = Stroke(width = 1.5f)
                )
            }

            "shield" -> {
                val path = Path().apply {
                    moveTo(cx, cy - s * 1.1f)
                    lineTo(cx + s * 1.0f, cy - s * 0.5f)
                    lineTo(cx + s * 1.0f, cy + s * 0.3f)
                    cubicTo(cx + s * 1.0f, cy + s * 0.9f, cx, cy + s * 1.2f, cx, cy + s * 1.2f)
                    cubicTo(
                        cx,
                        cy + s * 1.2f,
                        cx - s * 1.0f,
                        cy + s * 0.9f,
                        cx - s * 1.0f,
                        cy + s * 0.3f
                    )
                    lineTo(cx - s * 1.0f, cy - s * 0.5f)
                    close()
                }
                drawPath(path, color = color, style = Stroke(width = 2.5f))
                drawPath(path, color = color.copy(alpha = 0.15f))
            }
        }
    }
}

@Composable
fun BadgeUnlockToast(badge: CineBadge, onDismiss: () -> Unit) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    LaunchedEffect(Unit) {
        delay(3000)
        onDismiss()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface2, RoundedCornerShape(14.dp))
            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        BadgeSymbolCanvas(
            color = badge.ringColor,
            type = badge.type.name.lowercase(),
            modifier = Modifier.size(40.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Badge unlocked!",
                fontFamily = bebasNeue,
                fontSize = 15.sp,
                letterSpacing = 1.sp,
                color = Gold
            )
            Text(
                text = badge.name,
                fontSize = 13.sp,
                color = TextColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = badge.description,
                fontSize = 11.sp,
                color = Muted
            )
        }
    }
}

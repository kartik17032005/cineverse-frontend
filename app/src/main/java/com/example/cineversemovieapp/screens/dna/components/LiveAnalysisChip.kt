package com.example.cineversemovieapp.screens.dna.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LiveAnalysisChip() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color(0xFFFFB300).copy(alpha = 0.3f),
                shape = RoundedCornerShape(50)
            )
            .background(Color(0xFF1A1A24), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Pulsing background circle
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .scale(scale)
                    .background(Color(0xFFFFB300).copy(alpha = 1f - scale/1.5f), CircleShape)
            )
            // Solid center dot
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Color(0xFFFFB300), CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "LIVE ANALYSIS",
            color = Color(0xFFFFB300),
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
    }
}

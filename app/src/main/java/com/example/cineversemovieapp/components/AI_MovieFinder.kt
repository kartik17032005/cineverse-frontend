package com.example.cineversemovieapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold

@Composable
fun AI_Movie_Finder(
    navController: NavController,
){
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    // ── AI Movie Finder (Moved for better visibility) ──
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.horizontalGradient(listOf(gold.copy(alpha = 0.15f), Color(0xFF1A1A24)))
            )
            .border(1.dp, gold.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .clickable { navController.navigate(Routes.AiRecommend.route) }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = "AI Finder",
                tint = gold,
                modifier = Modifier.size(24.dp)
            )

            Column {
                Text(
                    text = "AI MOVIE FINDER",
                    fontFamily = bebasNeue,
                    fontSize = 16.sp,
                    letterSpacing = 2.sp,
                    color = Color.White
                )

                Text(
                    text = "Describe what you're in the mood for",
                    fontSize = 11.sp,
                    color = Color(0xFF5E5C68),
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}
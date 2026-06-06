package com.example.cineversemovieapp.screens.dna.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineversemovieapp.data.remote.dto.EmotionBreakdownItem

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
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = emotion.label,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(26.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.03f))
                    ) {
                        // Premium gradient bar with rounded ends
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(emotion.percentage.toFloat() / 100f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            goldColor.copy(alpha = 0.5f),
                                            goldColor
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            if (emotion.percentage > 12) {
                                Text(
                                    text = "${emotion.percentage}%",
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            }
                        }
                    }
                }
                if (emotion != items.lastOrNull()) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // X-Axis labels
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
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

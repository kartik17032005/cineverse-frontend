package com.example.cineversemovieapp.screens.dna.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.ui.theme.gold

@Composable
fun InfoCard(
    headerText: String,
    value: Int,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    Card(
        modifier = modifier.height(95.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14141A)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = gold.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = headerText.uppercase(),
                    fontSize = 9.sp,
                    color = Color.White.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = value.toString(),
                color = gold,
                fontSize = 32.sp,
                fontFamily = bebasNeue,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                lineHeight = 32.sp
            )
        }
    }
}

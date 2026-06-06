package com.example.cineversemovieapp.screens.dna.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IntensityCurveChart(
    goldColor: Color,
    dataPoints: List<Float>,
    labels: List<String> = emptyList(),
    duration: String
) {
    if (dataPoints.isEmpty()) return

    val totalMinutes = duration.filter { it.isDigit() }.toIntOrNull() ?: 120
    val timeLabels = listOf(
        "0 min",
        "${totalMinutes / 4} min",
        "${totalMinutes / 2} min",
        "${(totalMinutes * 3) / 4} min",
        "$totalMinutes min"
    )

    Card(
        modifier = Modifier.fillMaxWidth().height(240.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14141A)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 32.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
            ) {
                val w = size.width
                val h = size.height
                val maxVal = 100f
                val minVal = 0f
                val range = maxVal - minVal

                fun xOf(i: Int) = if (dataPoints.size > 1) i * w / (dataPoints.size - 1) else w / 2
                fun yOf(v: Float) = h - ((v - minVal) / range) * h

                val linePath = Path()
                if (dataPoints.size > 1) {
                    linePath.moveTo(xOf(0), yOf(dataPoints[0]))
                    for (i in 1 until dataPoints.size) {
                        val cpX = (xOf(i - 1) + xOf(i)) / 2f
                        linePath.cubicTo(
                            cpX, yOf(dataPoints[i - 1]),
                            cpX, yOf(dataPoints[i]),
                            xOf(i), yOf(dataPoints[i])
                        )
                    }
                }

                val fillPath = Path()
                if (!linePath.isEmpty) {
                    fillPath.addPath(linePath)
                    fillPath.lineTo(xOf(dataPoints.size - 1), h)
                    fillPath.lineTo(xOf(0), h)
                    fillPath.close()

                    // 1. Gradient Fill
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(goldColor.copy(alpha = 0.3f), Color.Transparent),
                            startY = 0f, endY = h
                        )
                    )

                    // 2. Main Curve Line
                    drawPath(
                        path = linePath,
                        color = goldColor,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // 3. Dots on peak moments or every point
                    dataPoints.forEachIndexed { i, v ->
                        val centerX = xOf(i)
                        val centerY = yOf(v)
                        
                        // Draw point dot
                        drawCircle(
                            color = if (v >= 80f) goldColor else goldColor.copy(alpha = 0.5f),
                            radius = (if (v >= 80f) 5.dp else 3.dp).toPx(),
                            center = Offset(centerX, centerY)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                timeLabels.forEach { label ->
                    Text(
                        text = label,
                        color = Color.White.copy(alpha = 0.25f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

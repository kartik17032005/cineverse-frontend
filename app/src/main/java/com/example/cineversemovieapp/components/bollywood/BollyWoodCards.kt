package com.example.cineversemovieapp.components.bollywood

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.data.tmdb.TmdbMovie
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold

@Composable
fun BollywoodCards(
    movie: TmdbMovie,
    rank: Int,
    navController: NavController
) {
    val gold = gold
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val bg = Color(0xFF0A0A0F)

    Box(
        modifier = Modifier
            .height(200.dp)
            .width(280.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable {
                navController.navigate(
                    Routes.MovieDetail.createRoute(movie.id)
                )
            }
    ) {

        // ── Backdrop image ──
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.backdropUrl)
                .crossfade(true)
                .build(),
            contentDescription = movie.displayTitle,
            contentScale = ContentScale.Crop,   // 👈 Crop not Fit
            modifier = Modifier.fillMaxSize()
        )

        // ── Dark gradient overlay ──
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // ── Rank badge (top left) ── 👈 box badge instead of large text
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(26.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(gold)
                .align(Alignment.TopStart),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                fontSize = 13.sp,
                fontFamily = bebasNeue,
                fontWeight = FontWeight.Bold,
                color = bg
            )
        }

        // ── Content at bottom ──
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, bottom = 10.dp, end = 12.dp)
        ) {

            // ── Title ──
            Text(
                text = movie.displayTitle,
                fontSize = 14.sp,
                fontFamily = bebasNeue,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ── Genre + Rating + Year in one row ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                // Genre pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(gold.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = movie.getGenreName(),
                        fontSize = 9.sp,
                        color = gold,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Dot separator
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFF5E5C68))
                )

                // Star icon
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = gold,
                    modifier = Modifier.size(10.dp)
                )

                // Rating
                Text(
                    text = movie.rating,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = gold
                )

                // Dot separator
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFF5E5C68))
                )

                // Release year
                Text(
                    text = movie.releaseYear,
                    fontSize = 10.sp,
                    color = Color(0xFF5E5C68),
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}
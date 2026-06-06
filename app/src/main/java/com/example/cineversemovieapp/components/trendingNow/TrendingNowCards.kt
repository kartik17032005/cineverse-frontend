package com.example.cineversemovieapp.components.trendingNow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cineversemovieapp.data.tmdb.TmdbMovie
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold

@Composable
fun TrendingNowCard(
    movie: TmdbMovie,
    rank: Int,
    navController: NavController
) {
    val gold = gold
    val bg = Color(0xFF0A0A0F)
    val surface = Color(0xFF1A1A24)

    Column(
        modifier = Modifier
            .width(170.dp)
            .padding(3.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable {
                    navController.navigate(
                        Routes.MovieDetail.createRoute(
                            movie.id,
                            isTv = movie.mediaType == "tv" || movie.title == null
                        )
                    )
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = surface)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.posterUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = movie.displayTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // ── Rank badge (top left) ──
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(22.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(gold)
                        .align(Alignment.TopStart),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = rank.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = bg
                    )
                }

                // ── Rating badge (bottom right) ──
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                        .align(Alignment.BottomEnd),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = gold,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = movie.rating,
                            color = gold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // ── Title below card ──
        Text(
            text = movie.displayTitle,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        )

        // ── Genre below title ──
        Text(
            text = "${movie.getGenreName()} ${movie.releaseYear}",
            color = Color(0xFF5E5C68),
            fontSize = 13.sp,
        )
    }
}

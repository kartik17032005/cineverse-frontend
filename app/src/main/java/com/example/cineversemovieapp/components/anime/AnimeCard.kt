package com.example.cineversemovieapp.components.anime

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.cineversemovieapp.data.tmdb.TmdbTvShow
import com.example.cineversemovieapp.navigation.Routes

@Composable
fun AnimeCard(
    show: TmdbTvShow,
    rank: Int,
    navController: NavController
) {
    val purple = Color(0xFFa882e6)
    val purpleDim = Color(0x4Da882e6)
    val purpleBorder = Color(0x4Da882e6)
    val bg = Color(0xFF0A0A0F)
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    Column(
        modifier = Modifier.width(200.dp)
    ) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(340.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(
                    width = 1.5.dp,
                    color = purpleBorder,
                    shape = RoundedCornerShape(14.dp)
                )
                .clickable {
                    // Navigate with isTv = true since these are TV shows/Anime
                    navController.navigate(
                        Routes.MovieDetail.createRoute(show.id, isTv = true)
                    )
                }
        ) {

            // ── Poster Image ──
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(show.posterUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = show.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            // ── Dark gradient overlay ──
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
            )

            // ── Purple top glow bar ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(purple)
                    .align(Alignment.TopCenter)
            )

            // ── Rank badge top left ──
            Box(
                modifier = Modifier
                    .padding(7.dp)
                    .size(22.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(purple)
                    .align(Alignment.TopStart),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    fontSize = 11.sp,
                    fontFamily = bebasNeue,
                    fontWeight = FontWeight.Bold,
                    color = bg
                )
            }

            // ── Anime genre tag top right ──
            Box(
                modifier = Modifier
                    .padding(7.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(purpleDim)
                    .border(1.dp, purpleBorder, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    text = show.getAnimGenreTag(),
                    fontSize = 8.sp,
                    color = purple,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }

            // ── Rating bottom center ──
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.55f))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = purple,
                    modifier = Modifier.size(10.dp)
                )
                Text(
                    text = show.rating,
                    color = purple,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ── Show Title ──
        Text(
            text = show.name ?: "Unknown",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(2.dp))

        // ── Year · Episodes ──
        Text(
            text = "${show.releaseYear} · ${show.episodeCount}",
            color = Color(0xFF5E5C68),
            fontSize = 10.sp
        )
    }
}

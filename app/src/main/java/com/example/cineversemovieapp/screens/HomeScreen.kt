package com.example.cineversemovieapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.components.SearchBar
import com.example.cineversemovieapp.components.anime.AnimeRow
import com.example.cineversemovieapp.components.bannerItems.FeaturedPager
import com.example.cineversemovieapp.components.bollywood.BollywoodRow
import com.example.cineversemovieapp.components.genres.CategoryRow
import com.example.cineversemovieapp.components.trendingNow.TrendingNowRow
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.CineverseMovieAppTheme
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.AnimeState
import com.example.cineversemovieapp.viewmodel.AuthViewModel
import com.example.cineversemovieapp.viewmodel.MovieState
import com.example.cineversemovieapp.viewmodel.MovieViewModel
import com.example.cineversemovieapp.viewmodel.TmdbState
import com.example.cineversemovieapp.viewmodel.TmdbViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    tmdbViewModel: TmdbViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val bg = Color(0xFF0A0A0F)
    val movieViewModel: MovieViewModel = viewModel()
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    LaunchedEffect(Unit) {
        movieViewModel.getFeaturedMovies()
        tmdbViewModel.getTrendingMovies()
        tmdbViewModel.getTopRatedMovies()
        tmdbViewModel.getBollywoodMovies()
        tmdbViewModel.getAnimeShows()
        tmdbViewModel.getHollywoodClassics()
        tmdbViewModel.getKDramas()
        tmdbViewModel.getAwardWinners()
        tmdbViewModel.getNowPlaying()
        tmdbViewModel.getPopularTvShows()
        tmdbViewModel.getUpcomingMovies()
        tmdbViewModel.resetMovieVideos()
        authViewModel.getWatchlist()
    }

    Scaffold(
        containerColor = bg,
        topBar = {
            TopAppBar(
                title = { CineverseTopBar(navController = navController) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bg)
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MainContent(
                movieViewModel = movieViewModel,
                tmdbViewModel = tmdbViewModel,
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}

@Composable
fun CineverseTopBar(navController: NavController) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val surfaceColor = Color(0xFF1A1A24)
    val bg = Color(0xFF0A0A0F)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(gold),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Movie,
                    contentDescription = null,
                    tint = bg,
                    modifier = Modifier.size(17.dp)
                )
            }
            Text(
                text = "  CINEVERSE",
                color = gold,
                fontFamily = bebasNeue,
                fontSize = 23.sp,
                letterSpacing = 4.sp
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(surfaceColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFFf0ede8),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(gold)
                        .align(Alignment.TopEnd)
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(gold)
                    .clickable(onClick = {
                        navController.navigate(Routes.Profile.route)
                    }),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "K",
                    color = bg,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = bebasNeue
                )
            }
        }
    }
}

@Composable
fun MainContent(
    movieViewModel: MovieViewModel,
    tmdbViewModel: TmdbViewModel,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val purple = Color(0xFFa882e6)

    val genres = listOf(
        "🔥 All" to null,
        "🎬 Action" to 28,
        "🚀 Sci-Fi" to 878,
        "🎭 Drama" to 18,
        "😱 Thriller" to 53,
        "😂 Comedy" to 35
    )

    val querySearch = remember { mutableStateOf("") }
    val featuredState by movieViewModel.featuredMovies.collectAsState()
    val trendingState by tmdbViewModel.trendingMovies.collectAsState()
    val topRatedState by tmdbViewModel.topRatedMovies.collectAsState()
    val selectedGenre = remember { mutableStateOf("🔥 All") }
    val bollywoodState by tmdbViewModel.bollywoodMovies.collectAsState()
    val animeState by tmdbViewModel.animeShows.collectAsState()
    val classicsState by tmdbViewModel.hollywoodClassics.collectAsState()
    val kDramaState by tmdbViewModel.kDramas.collectAsState()
    val awardState by tmdbViewModel.awardWinners.collectAsState()
    val tvShowsState by tmdbViewModel.popularTvShows.collectAsState()
    val upcomingState by tmdbViewModel.upcomingMovies.collectAsState()
    val searchState by tmdbViewModel.searchResults.collectAsState()
    val nowPlayingState by tmdbViewModel.nowPlaying.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(text = "Good Evening 🎬", color = Color(0xFF474761), fontSize = 13.sp)

        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontFamily = bebasNeue,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                ) { append("WHAT ARE YOU ") }
                withStyle(
                    SpanStyle(
                        color = gold,
                        fontSize = 24.sp,
                        fontFamily = bebasNeue,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                ) { append("WATCHING ") }
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontFamily = bebasNeue,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                ) { append("TONIGHT?") }
            }
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            SearchBar(
                query = querySearch,
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable {
                        navController.navigate(Routes.Search.route)
                    }
            )
        }

        // ── AI Movie Finder (Premium Shortcut) ──
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

        // ── Featured Today (With fallback to TMDB Now Playing) ──
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = bebasNeue,
                        fontSize = 24.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("FEATURED ") }
                withStyle(
                    SpanStyle(
                        color = gold,
                        fontFamily = bebasNeue,
                        fontSize = 24.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("TODAY") }
            }
        )

        when {
            featuredState is MovieState.Loading || nowPlayingState is TmdbState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFF1E1E2A)),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = gold, strokeWidth = 2.dp) }
            }

            featuredState is MovieState.Success -> {
                val movies = (featuredState as MovieState.Success).movies
                if (movies.isNotEmpty()) {
                    FeaturedPager(
                        movies = movies,
                        navController = navController,
                        authViewModel = authViewModel
                    )
                } else if (nowPlayingState is TmdbState.Success) {
                    val tmdbMovies = (nowPlayingState as TmdbState.Success).movies
                    FeaturedPager(
                        movies = tmdbMovies,
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
            }

            featuredState is MovieState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFF1E1E2A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚠ ${(featuredState as MovieState.Error).message}",
                        color = Color(0xFFE84B4B),
                        fontSize = 13.sp
                    )
                }
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(4.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF474761).copy(alpha = 0.4f),
            thickness = 0.6.dp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // ── Categories ──
        CategoryRow(
            genres = genres.map { it.first },
            selectedGenre = selectedGenre.value,
            onGenreSelected = { genreName ->
                selectedGenre.value = genreName
                val genreId = genres.find { it.first == genreName }?.second
                if (genreId == null) {
                    tmdbViewModel.resetSearchResults()
                } else {
                    tmdbViewModel.searchMoviesByGenre(genreId)
                }
            }
        )

        when (searchState) {
            is TmdbState.Loading -> {
                CircularProgressIndicator(
                    color = gold,
                    strokeWidth = 2.dp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            is TmdbState.Success -> {
                val movies = (searchState as TmdbState.Success).movies
                if (movies.isNotEmpty()) {
                    Text(
                        text = "RESULTS",
                        color = gold,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp
                    )
                    TrendingNowRow(
                        movies = movies,
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            is TmdbState.Error -> {
                Text(
                    text = "⚠ ${(searchState as TmdbState.Error).message}",
                    color = Color(0xFFE84B4B)
                )
            }

            else -> {}
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── Movie Content Rows ──
        SectionHeader("TRENDING", "NOW", bebasNeue)
        when (trendingState) {
            is TmdbState.Loading -> CircularProgressIndicator(color = gold, modifier = Modifier.padding(16.dp))
            is TmdbState.Success -> TrendingNowRow((trendingState as TmdbState.Success).movies, navController)
            is TmdbState.Error -> ErrorDisplay((trendingState as TmdbState.Error).message)
            else -> {}
        }

        SectionHeader("TOP", "RATED", bebasNeue)
        when (topRatedState) {
            is TmdbState.Loading -> CircularProgressIndicator(color = gold, modifier = Modifier.padding(16.dp))
            is TmdbState.Success -> TrendingNowRow((topRatedState as TmdbState.Success).movies, navController)
            is TmdbState.Error -> ErrorDisplay((topRatedState as TmdbState.Error).message)
            else -> {}
        }

        SectionHeader("BOLLYWOOD", "HITS", bebasNeue)
        when (bollywoodState) {
            is TmdbState.Loading -> CircularProgressIndicator(color = gold, modifier = Modifier.padding(16.dp))
            is TmdbState.Success -> BollywoodRow((bollywoodState as TmdbState.Success).movies, navController)
            is TmdbState.Error -> ErrorDisplay((bollywoodState as TmdbState.Error).message)
            else -> {}
        }

        SectionHeader("ANIME", "COLLECTION", bebasNeue, purple)
        when (animeState) {
            is AnimeState.Loading -> CircularProgressIndicator(color = purple, modifier = Modifier.padding(16.dp))
            is AnimeState.Success -> AnimeRow((animeState as AnimeState.Success).shows, navController)
            is AnimeState.Error -> ErrorDisplay((animeState as AnimeState.Error).message)
            else -> {}
        }

        SectionHeader("HOLLYWOOD", "CLASSICS", bebasNeue)
        when (classicsState) {
            is TmdbState.Loading -> CircularProgressIndicator(color = gold, modifier = Modifier.padding(16.dp))
            is TmdbState.Success -> TrendingNowRow((classicsState as TmdbState.Success).movies, navController)
            is TmdbState.Error -> ErrorDisplay((classicsState as TmdbState.Error).message)
            else -> {}
        }

        SectionHeader("AWARD", "WINNERS", bebasNeue)
        when (awardState) {
            is TmdbState.Loading -> CircularProgressIndicator(color = gold, modifier = Modifier.padding(16.dp))
            is TmdbState.Success -> TrendingNowRow((awardState as TmdbState.Success).movies, navController)
            is TmdbState.Error -> ErrorDisplay((awardState as TmdbState.Error).message)
            else -> {}
        }

        SectionHeader("K", "DRAMA", bebasNeue)
        when (kDramaState) {
            is AnimeState.Loading -> CircularProgressIndicator(color = gold, modifier = Modifier.padding(16.dp))
            is AnimeState.Success -> AnimeRow((kDramaState as AnimeState.Success).shows, navController)
            is AnimeState.Error -> ErrorDisplay((kDramaState as AnimeState.Error).message)
            else -> {}
        }

        SectionHeader("POPULAR", "TV SHOWS", bebasNeue)
        when (tvShowsState) {
            is AnimeState.Loading -> CircularProgressIndicator(color = gold, modifier = Modifier.padding(16.dp))
            is AnimeState.Success -> AnimeRow((tvShowsState as AnimeState.Success).shows, navController)
            is AnimeState.Error -> ErrorDisplay((tvShowsState as AnimeState.Error).message)
            else -> {}
        }

        SectionHeader("COMING", "SOON", bebasNeue)
        when (upcomingState) {
            is TmdbState.Loading -> CircularProgressIndicator(color = gold, modifier = Modifier.padding(16.dp))
            is TmdbState.Success -> TrendingNowRow((upcomingState as TmdbState.Success).movies, navController)
            is TmdbState.Error -> ErrorDisplay((upcomingState as TmdbState.Error).message)
            else -> {}
        }
    }
}

@Composable
fun SectionHeader(word1: String, word2: String, fontFamily: FontFamily, color2: Color = gold) {
    Text(
        buildAnnotatedString {
            withStyle(SpanStyle(color = Color.White, fontFamily = fontFamily, fontSize = 20.sp, letterSpacing = 2.sp)) { append("$word1 ") }
            withStyle(SpanStyle(color = color2, fontFamily = fontFamily, fontSize = 20.sp, letterSpacing = 2.sp)) { append(word2) }
        },
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun ErrorDisplay(message: String) {
    Text(text = "⚠ $message", color = Color(0xFFE84B4B), fontSize = 13.sp)
}

// ── Bottom Nav ──
@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String?
) {
    val bg = Color(0xFF111118)
    val muted = Color(0xFF5E5C68)
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg)
            .border(width = 0.5.dp, color = Color.White.copy(alpha = 0.07f), shape = RoundedCornerShape(0.dp))
            .padding(top = 10.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(Modifier.weight(1f), Icons.Filled.Home, "Home", currentRoute == Routes.Home.route, gold, muted, bebasNeue) {
            navController.navigate(Routes.Home.route) { popUpTo(Routes.Home.route) { inclusive = true } }
        }
        BottomNavItem(Modifier.weight(1f), Icons.Filled.Search, "Search", currentRoute == Routes.Search.route, gold, muted, bebasNeue) {
            navController.navigate(Routes.Search.route)
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (currentRoute == Routes.Reels.route) Brush.linearGradient(listOf(gold, Color(0xFFa87830))) else Brush.linearGradient(listOf(Color(0xFF1A1A24), Color(0xFF1A1A24))))
                    .clickable { navController.navigate(Routes.Reels.route) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PlayArrow, "Reels", tint = if (currentRoute == Routes.Reels.route) Color(0xFF0A0A0F) else gold, modifier = Modifier.size(26.dp))
            }
        }
        BottomNavItem(Modifier.weight(1f), Icons.Filled.Bookmark, "Watchlist", currentRoute == Routes.Watchlist.route, gold, muted, bebasNeue) {
            navController.navigate(Routes.Watchlist.route)
        }
        BottomNavItem(Modifier.weight(1f), Icons.Filled.Person, "Profile", currentRoute == Routes.Profile.route, gold, muted, bebasNeue) {
            navController.navigate(Routes.Profile.route)
        }
    }
}

@Composable
fun BottomNavItem(modifier: Modifier = Modifier, icon: ImageVector, label: String, isSelected: Boolean, gold: Color, muted: Color, bebasNeue: FontFamily, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier.clickable { onClick() }) {
        Icon(icon, label, tint = if (isSelected) gold else muted, modifier = Modifier.size(22.dp))
        Text(label, fontSize = 10.sp, color = if (isSelected) gold else muted, fontFamily = bebasNeue, letterSpacing = 0.5.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CineverseTopBarPreview() {
    val navController = rememberNavController()
    CineverseMovieAppTheme { CineverseTopBar(navController = navController) }
}

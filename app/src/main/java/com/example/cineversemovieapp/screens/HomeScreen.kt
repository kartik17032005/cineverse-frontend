package com.example.cineversemovieapp.screens

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
import androidx.compose.ui.platform.LocalContext
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
import com.example.cineversemovieapp.components.AI_Movie_Finder
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
                navController = navController
            )
        }
    }
}

@Composable
fun CineverseTopBar(navController: NavController) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val gold = gold
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
    navController: NavController
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val gold = gold
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
    val movieVideosState by tmdbViewModel.movieVideos.collectAsState()
    val context = LocalContext.current

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

        AI_Movie_Finder(navController = navController)

        // ── Featured Today ──
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
                        navController = navController
                    )
                } else if (nowPlayingState is TmdbState.Success) {
                    val tmdbMovies = (nowPlayingState as TmdbState.Success).movies
                    FeaturedPager(
                        movies = tmdbMovies,
                        navController = navController
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

        // ── Trending Now ──
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("TRENDING ") }
                withStyle(
                    SpanStyle(
                        color = gold,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("NOW") }
            }
        )

        when (trendingState) {
            is TmdbState.Loading -> CircularProgressIndicator(
                color = gold,
                strokeWidth = 2.dp,
                modifier = Modifier.padding(16.dp)
            )

            is TmdbState.Success -> TrendingNowRow(
                movies = (trendingState as TmdbState.Success).movies,
                navController = navController
            )

            is TmdbState.Error -> Text(
                text = "⚠ ${(trendingState as TmdbState.Error).message}",
                color = Color(0xFFE84B4B),
                fontSize = 13.sp
            )

            else -> {}
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── Top Rated ──
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("TOP ") }
                withStyle(
                    SpanStyle(
                        color = gold,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("RATED") }
            }
        )

        when (topRatedState) {
            is TmdbState.Loading -> CircularProgressIndicator(
                color = gold,
                strokeWidth = 2.dp,
                modifier = Modifier.padding(16.dp)
            )

            is TmdbState.Success -> TrendingNowRow(
                movies = (topRatedState as TmdbState.Success).movies,
                navController = navController
            )

            is TmdbState.Error -> Text(
                text = "⚠ ${(topRatedState as TmdbState.Error).message}",
                color = Color(0xFFE84B4B),
                fontSize = 13.sp
            )

            else -> {}
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── Bollywood Hits ──
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("BOLLYWOOD ") }
                withStyle(
                    SpanStyle(
                        color = gold,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("HITS") }
            }
        )

        when (bollywoodState) {
            is TmdbState.Loading -> CircularProgressIndicator(
                color = gold,
                strokeWidth = 2.dp,
                modifier = Modifier.padding(16.dp)
            )

            is TmdbState.Success -> BollywoodRow(
                movies = (bollywoodState as TmdbState.Success).movies,
                navController = navController
            )

            is TmdbState.Error -> Text(
                text = "⚠ ${(bollywoodState as TmdbState.Error).message}",
                color = Color(0xFFE84B4B),
                fontSize = 13.sp
            )

            else -> {}
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── Anime Collection ──
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("ANIME ") }
                withStyle(
                    SpanStyle(
                        color = purple,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("COLLECTION") }
            }
        )

        when (animeState) {
            is AnimeState.Loading -> CircularProgressIndicator(
                color = purple,
                strokeWidth = 2.dp,
                modifier = Modifier.padding(16.dp)
            )

            is AnimeState.Success -> AnimeRow(
                shows = (animeState as AnimeState.Success).shows,
                navController = navController
            )

            is AnimeState.Error -> Text(
                text = "⚠ ${(animeState as AnimeState.Error).message}",
                color = Color(0xFFE84B4B),
                fontSize = 13.sp
            )

            else -> {}
        }

        // ── Hollywood Classics ──
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("HOLLYWOOD ") }
                withStyle(
                    SpanStyle(
                        color = gold,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("CLASSICS") }
            }
        )

        when (classicsState) {
            is TmdbState.Loading -> CircularProgressIndicator(
                color = gold,
                strokeWidth = 2.dp,
                modifier = Modifier.padding(16.dp)
            )

            is TmdbState.Success -> TrendingNowRow(
                movies = (classicsState as TmdbState.Success).movies,
                navController = navController
            )

            is TmdbState.Error -> Text(
                text = "⚠ ${(classicsState as TmdbState.Error).message}",
                color = Color(0xFFE84B4B),
                fontSize = 13.sp
            )

            else -> {}
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── Award Winners ──
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("AWARD ") }
                withStyle(
                    SpanStyle(
                        color = gold,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("WINNERS") }
            }
        )

        when (awardState) {
            is TmdbState.Loading -> CircularProgressIndicator(
                color = gold,
                strokeWidth = 2.dp,
                modifier = Modifier.padding(16.dp)
            )

            is TmdbState.Success -> TrendingNowRow(
                movies = (awardState as TmdbState.Success).movies,
                navController = navController
            )

            is TmdbState.Error -> Text(
                text = "⚠ ${(awardState as TmdbState.Error).message}",
                color = Color(0xFFE84B4B),
                fontSize = 13.sp
            )

            else -> {}
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── K-Drama ──
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("K ") }
                withStyle(
                    SpanStyle(
                        color = gold,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("DRAMA") }
            }
        )

        when (kDramaState) {
            is AnimeState.Loading -> CircularProgressIndicator(
                color = gold,
                strokeWidth = 2.dp,
                modifier = Modifier.padding(16.dp)
            )

            is AnimeState.Success -> AnimeRow(
                shows = (kDramaState as AnimeState.Success).shows,
                navController = navController
            )

            is AnimeState.Error -> Text(
                text = "⚠ ${(kDramaState as AnimeState.Error).message}",
                color = Color(0xFFE84B4B),
                fontSize = 13.sp
            )

            else -> {}
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── Popular TV Shows ──
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("POPULAR ") }
                withStyle(
                    SpanStyle(
                        color = gold,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("TV SHOWS") }
            }
        )

        when (tvShowsState) {
            is AnimeState.Loading -> CircularProgressIndicator(
                color = gold,
                strokeWidth = 2.dp,
                modifier = Modifier.padding(16.dp)
            )

            is AnimeState.Success -> AnimeRow(
                shows = (tvShowsState as AnimeState.Success).shows,
                navController = navController
            )

            is AnimeState.Error -> Text(
                text = "⚠ ${(tvShowsState as AnimeState.Error).message}",
                color = Color(0xFFE84B4B),
                fontSize = 13.sp
            )

            else -> {}
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── Coming Soon ──
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("COMING ") }
                withStyle(
                    SpanStyle(
                        color = gold,
                        fontFamily = bebasNeue,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    )
                ) { append("SOON") }
            }
        )

        when (upcomingState) {
            is TmdbState.Loading -> CircularProgressIndicator(
                color = gold,
                strokeWidth = 2.dp,
                modifier = Modifier.padding(16.dp)
            )

            is TmdbState.Success -> TrendingNowRow(
                movies = (upcomingState as TmdbState.Success).movies,
                navController = navController
            )

            is TmdbState.Error -> Text(
                text = "⚠ ${(upcomingState as TmdbState.Error).message}",
                color = Color(0xFFE84B4B),
                fontSize = 13.sp
            )

            else -> {}
        }

    }
}

// ── Bottom Nav ──
@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String?
) {
    val gold = gold
    val bg = Color(0xFF111118)
    val muted = Color(0xFF5E5C68)
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg)
            .border(
                width = 0.5.dp,
                color = Color.White.copy(alpha = 0.07f),
                shape = RoundedCornerShape(0.dp)
            )
            .padding(top = 10.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home
        BottomNavItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.Home,
            label = "Home",
            isSelected = currentRoute == Routes.Home.route,
            gold = gold,
            muted = muted,
            bebasNeue = bebasNeue,
            onClick = {
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.Home.route) { inclusive = true }
                }
            }
        )

        // Search
        BottomNavItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.Search,
            label = "Search",
            isSelected = currentRoute == Routes.Search.route,
            gold = gold,
            muted = muted,
            bebasNeue = bebasNeue,
            onClick = { navController.navigate(Routes.Search.route) }
        )

        // Reels — center button
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (currentRoute == Routes.Reels.route)
                            Brush.linearGradient(listOf(gold, Color(0xFFa87830)))
                        else
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF1A1A24),
                                    Color(0xFF1A1A24)
                                )
                            )
                    )
                    .clickable { navController.navigate(Routes.Reels.route) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Reels",
                    tint = if (currentRoute == Routes.Reels.route)
                        Color(0xFF0A0A0F)
                    else
                        gold,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // Watchlist
        BottomNavItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.Bookmark,
            label = "Watchlist",
            isSelected = currentRoute == Routes.Watchlist.route,
            gold = gold,
            muted = muted,
            bebasNeue = bebasNeue,
            onClick = { navController.navigate(Routes.Watchlist.route) }
        )

        // Profile
        BottomNavItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.Person,
            label = "Profile",
            isSelected = currentRoute == Routes.Profile.route,
            gold = gold,
            muted = muted,
            bebasNeue = bebasNeue,
            onClick = { navController.navigate(Routes.Profile.route) }
        )
    }
}

// ── Single Nav Item ──
@Composable
fun BottomNavItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    gold: Color,
    muted: Color,
    bebasNeue: FontFamily,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) gold else muted,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isSelected) gold else muted,
            fontFamily = bebasNeue,
            letterSpacing = 0.5.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CineverseTopBarPreview() {
    val navController = rememberNavController()
    CineverseMovieAppTheme {
        CineverseTopBar(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val navController = rememberNavController()
    CineverseMovieAppTheme {
        BottomNavBar(
            navController = navController,
            currentRoute = Routes.Home.route
        )
    }
}

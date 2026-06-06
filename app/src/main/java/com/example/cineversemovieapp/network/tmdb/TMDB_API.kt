package com.example.cineversemovieapp.network.tmdb

import com.example.cineversemovieapp.data.tmdb.TmdbCreditsResponse
import com.example.cineversemovieapp.data.tmdb.TmdbMovieDetail
import com.example.cineversemovieapp.data.tmdb.TmdbTvResponse
import com.example.cineversemovieapp.models.TmdbResponse
import com.example.cineversemovieapp.models.TmdbVideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDB_API {
    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String
    ): Response<TmdbResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String
    ): Response<TmdbResponse>

    // ── Use Multi Search to find both Movies and Animes/TV Shows ──
    @GET("search/multi")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Response<TmdbResponse>

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int
    ): Response<TmdbResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbVideosResponse>

    // ── TV Videos ──
    @GET("tv/{tv_id}/videos")
    suspend fun getTvVideos(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbVideosResponse>

    //BOLLYWOOD MOVIES
    @GET("discover/movie")
    suspend fun getBollywoodMovies(
        @Query("api_key") apiKey: String,
        @Query("with_original_language") language: String = "hi",
        @Query("region") region: String = "IN",
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Response<TmdbResponse>

    // ── Anime Shows ──
    @GET("discover/tv")
    suspend fun getAnimeShows(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int = 16,
        @Query("with_original_language") language: String = "ja",
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Response<TmdbTvResponse>

    @GET("discover/movie")
    suspend fun getHollywoodClassics(
        @Query("api_key") apiKey: String,
        @Query("primary_release_date.gte") from: String = "1990-01-01",
        @Query("primary_release_date.lte") to: String = "2010-12-31",
        @Query("with_original_language") language: String = "en",
        @Query("sort_by") sortBy: String = "vote_average.desc",
        @Query("vote_count.gte") minVotes: Int = 1000
    ): Response<TmdbResponse>

    @GET("discover/tv")
    suspend fun getKDramas(
        @Query("api_key") apiKey: String,
        @Query("with_original_language") language: String = "ko",
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Response<TmdbTvResponse>

    @GET("discover/movie")
    suspend fun getAwardWinners(
        @Query("api_key") apiKey: String,
        @Query("sort_by") sortBy: String = "vote_average.desc",
        @Query("vote_count.gte") minVotes: Int = 5000,
        @Query("vote_average.gte") minRating: Double = 8.0
    ): Response<TmdbResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String
    ): Response<TmdbResponse>

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("api_key") apiKey: String
    ): Response<TmdbTvResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String
    ): Response<TmdbResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbMovieDetail>

    // ── TV Details ──
    @GET("tv/{tv_id}")
    suspend fun getTvDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbMovieDetail>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbCreditsResponse>

    // ── TV Credits ──
    @GET("tv/{tv_id}/credits")
    suspend fun getTvCredits(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbCreditsResponse>

    @GET("movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendations(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbResponse>

    @GET("tv/{tv_id}/recommendations")
    suspend fun getTvRecommendations(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbTvResponse>
}

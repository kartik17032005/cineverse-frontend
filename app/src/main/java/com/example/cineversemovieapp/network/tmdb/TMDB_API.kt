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

    @GET("search/movie")
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

    //BOLLYWOOD MOVIES
    @GET("discover/movie")
    suspend fun getBollywoodMovies(
        @Query("api_key") apiKey: String,
        @Query("with_original_language") language: String = "hi",  //--> HINDI
        @Query("region") region: String = "IN", //--> India
        @Query("sort_by") sortBy: String = "popularity.desc" //---> Most Popular First
    ): Response<TmdbResponse>

    //Anime
    // ── Anime Shows ──
    @GET("discover/tv")
    suspend fun getAnimeShows(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int = 16,
        @Query("with_original_language") language: String = "ja",
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Response<TmdbTvResponse>


    // ── Hollywood Classics ──
    @GET("discover/movie")
    suspend fun getHollywoodClassics(
        @Query("api_key") apiKey: String,
        @Query("primary_release_date.gte") from: String = "1990-01-01",
        @Query("primary_release_date.lte") to: String = "2010-12-31",
        @Query("with_original_language") language: String = "en",
        @Query("sort_by") sortBy: String = "vote_average.desc",
        @Query("vote_count.gte") minVotes: Int = 1000
    ): Response<TmdbResponse>

    // ── K-Drama ──
    @GET("discover/tv")
    suspend fun getKDramas(
        @Query("api_key") apiKey: String,
        @Query("with_original_language") language: String = "ko",
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Response<TmdbTvResponse>

    // ── Award Winners ──
    @GET("discover/movie")
    suspend fun getAwardWinners(
        @Query("api_key") apiKey: String,
        @Query("sort_by") sortBy: String = "vote_average.desc",
        @Query("vote_count.gte") minVotes: Int = 5000,
        @Query("vote_average.gte") minRating: Double = 8.0
    ): Response<TmdbResponse>

    // ── Now Playing ──
    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String
    ): Response<TmdbResponse>

    // ── Popular TV Shows ──
    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("api_key") apiKey: String
    ): Response<TmdbTvResponse>

    // ── Upcoming Movies ──
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String
    ): Response<TmdbResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbMovieDetail>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbCreditsResponse>
}

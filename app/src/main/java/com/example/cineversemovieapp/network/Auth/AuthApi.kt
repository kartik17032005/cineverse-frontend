package com.example.cineversemovieapp.network.Auth

import com.example.cineversemovieapp.data.remote.dto.MovieDNA
import com.example.cineversemovieapp.models.AddWatchlistRequest
import com.example.cineversemovieapp.models.AiMovieResult
import com.example.cineversemovieapp.models.AiRecommendation
import com.example.cineversemovieapp.models.LoginRequest
import com.example.cineversemovieapp.models.LoginResponse
import com.example.cineversemovieapp.models.Movie
import com.example.cineversemovieapp.models.RegisterRequest
import com.example.cineversemovieapp.models.TmdbVideosResponse
import com.example.cineversemovieapp.models.UpdateUserRequest
import com.example.cineversemovieapp.models.UserResponse
import com.example.cineversemovieapp.models.WatchlistItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {
    @GET("movies")
    suspend fun getMovies(): Response<List<Movie>>

    @GET("movies/featured")
    suspend fun getFeaturedMovies(): Response<List<Movie>>

    @GET("movies/genre/{genre}")
    suspend fun getMoviesByGenre(@Path("genre") genre: String): Response<List<Movie>>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbVideosResponse>

    @GET("movies/{id}")
    suspend fun getMovieById(@Path("id") id: Long): Response<Movie>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @PUT("auth/update/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body request: UpdateUserRequest
    ): Response<UserResponse>

    @GET("auth/user/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<UserResponse>

    @GET("watchlist/{userId}")
    suspend fun getWatchlist(@Path("userId") userId: Long): Response<List<WatchlistItem>>

    @POST("watchlist/{userId}")
    suspend fun addToWatchlist(
        @Path("userId") userId: Long,
        @Body request: AddWatchlistRequest
    ): Response<WatchlistItem>

    @DELETE("watchlist/{userId}/{tmdbId}")
    suspend fun removeFromWatchlist(
        @Path("userId") userId: Long,
        @Path("tmdbId") tmdbId: Long
    ): Response<String>

    @GET("watchlist/{userId}/check/{tmdbId}")
    suspend fun checkWatchlist(
        @Path("userId") userId: Long,
        @Path("tmdbId") tmdbId: Long
    ): Response<Boolean>

    @POST("ai/recommend")
    suspend fun getAiRecommendations(@Body request: AiRecommendation): Response<List<AiMovieResult>>

    @GET("api/dna/{movieId}")
    suspend fun getSceneDNA(@Path("movieId") movieId: Long): MovieDNA
}

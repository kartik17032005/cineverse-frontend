package com.example.cineversemovieapp.network.Auth

import com.example.cineversemovieapp.models.AddWatchlistRequest
import com.example.cineversemovieapp.models.AiMovieResult
import com.example.cineversemovieapp.models.AiRecommendation
import com.example.cineversemovieapp.models.LoginRequest
import com.example.cineversemovieapp.models.LoginResponse
import com.example.cineversemovieapp.models.Movie
import com.example.cineversemovieapp.models.RegisterRequest
import com.example.cineversemovieapp.models.RegisterResponse
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

//calls your springboot endpoint
interface AuthApi {
    @GET("movies")
    suspend fun getMovies(): Response<List<Movie>>

    @GET("movies/featured")
    suspend fun getFeaturedMovies(): Response<List<Movie>>

    @GET("movies/genre/{genre}")
    suspend fun getMoviesByGenre(@Path("genre") genre: String): Response<List<Movie>>

    // ── Movie Videos / Trailers ──
    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<TmdbVideosResponse>

    //get movies by id
    @GET("movies/{id}")
    suspend fun getMovieById(@Path("id") id: Long): Response<Movie>

    //register user
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<LoginResponse>    // 👈 now returns AuthResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>    // 👈 now returns AuthResponse

    @PUT("auth/update/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body request: UpdateUserRequest
    ): Response<UserResponse>

    @GET("auth/user/{id}")
    suspend fun getUserById(
        @Path("id") id: Long
    ): Response<UserResponse>

    // ── Watchlist ──
    @GET("watchlist/{userId}")
    suspend fun getWatchlist(
        @Path("userId") userId: Long
    ): Response<List<WatchlistItem>>

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
    suspend fun getAiRecommendations(
        @Body request: AiRecommendation
    ): Response<List<AiMovieResult>>
}
package com.example.cineversemovieapp.repository

import com.example.cineversemovieapp.network.tmdb.TmdbRetrofitInstance
import com.example.cineversemovieapp.utils.Constants

class TmdbRepository {

    private val api = TmdbRetrofitInstance.api
    private val apiKey = Constants.TMDB_API_KEY

    suspend fun getTrendingMovies() = api.getTrendingMovies(apiKey)

    suspend fun getTopRatedMovies() = api.getTopRatedMovies(apiKey)

    suspend fun searchMovies(query: String) = api.searchMovies(apiKey, query)

    suspend fun getMoviesByGenre(genreId: Int) = api.getMoviesByGenre(apiKey, genreId)

    suspend fun getBollyWoodMovies() = api.getBollywoodMovies(apiKey)

    suspend fun getAnimeShows() = api.getAnimeShows(apiKey)

    suspend fun getHollywoodClassics() = api.getHollywoodClassics(apiKey)  // 👈 new
    suspend fun getKDramas() = api.getKDramas(apiKey)                      // 👈 new
    suspend fun getAwardWinners() = api.getAwardWinners(apiKey)            // 👈 new
    suspend fun getNowPlaying() = api.getNowPlaying(apiKey)                // 👈 new
    suspend fun getPopularTvShows() = api.getPopularTvShows(apiKey)        // 👈 new
    suspend fun getUpcomingMovies() = api.getUpcomingMovies(apiKey)

    suspend fun getMovieDetails(movieId: Int) = api.getMovieDetails(movieId, apiKey)

    suspend fun getMovieCredits(movieId: Int) = api.getMovieCredits(movieId, apiKey)

    suspend fun getMovieVideos(movieId: Int) = api.getMovieVideos(movieId, apiKey)
}
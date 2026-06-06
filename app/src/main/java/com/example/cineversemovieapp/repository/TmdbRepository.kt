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

    suspend fun getBollywoodMovies() = api.getBollywoodMovies(apiKey)

    suspend fun getAnimeShows() = api.getAnimeShows(apiKey)

    suspend fun getHollywoodClassics() = api.getHollywoodClassics(apiKey)
    suspend fun getKDramas() = api.getKDramas(apiKey)
    suspend fun getAwardWinners() = api.getAwardWinners(apiKey)
    suspend fun getNowPlaying() = api.getNowPlaying(apiKey)
    suspend fun getPopularTvShows() = api.getPopularTvShows(apiKey)
    suspend fun getUpcomingMovies() = api.getUpcomingMovies(apiKey)

    suspend fun getMovieDetails(movieId: Int) = api.getMovieDetails(movieId, apiKey)

    // ── TV Details ──
    suspend fun getTvDetails(tvId: Int) = api.getTvDetails(tvId, apiKey)

    suspend fun getMovieCredits(movieId: Int) = api.getMovieCredits(movieId, apiKey)

    // ── TV Credits ──
    suspend fun getTvCredits(tvId: Int) = api.getTvCredits(tvId, apiKey)

    suspend fun getMovieVideos(movieId: Int) = api.getMovieVideos(movieId, apiKey)

    // ── TV Videos ──
    suspend fun getTvVideos(tvId: Int) = api.getTvVideos(tvId, apiKey)

    suspend fun getMovieRecommendations(movieId: Int) = api.getMovieRecommendations(movieId, apiKey)

    suspend fun getTvRecommendations(tvId: Int) = api.getTvRecommendations(tvId, apiKey)
}

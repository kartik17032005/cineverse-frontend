package com.example.cineversemovieapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineversemovieapp.models.Movie
import com.example.cineversemovieapp.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// viewmodel/MovieViewModel.kt
class MovieViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _featuredMovies = MutableStateFlow<MovieState>(MovieState.Idle)
    val featuredMovies: StateFlow<MovieState> = _featuredMovies

    private val _allMovies = MutableStateFlow<MovieState>(MovieState.Idle)
    val allMovies: StateFlow<MovieState> = _allMovies

    fun getFeaturedMovies() {
        viewModelScope.launch {
            _featuredMovies.value = MovieState.Loading
            try {
                val response = repository.getFeaturedMovies()
                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()!!

                    Log.d("FEATURED_API", "Movies: $movies") // 🔥 DEBUG

                    if (movies.isEmpty()) {
                        _featuredMovies.value = MovieState.Error("Empty list from backend")
                    } else {
                        _featuredMovies.value = MovieState.Success(movies)
                    }
                } else {
                    Log.d("FEATURED_API", "Error: ${response.errorBody()?.string()}")
                    _featuredMovies.value = MovieState.Error("API failed")
                }
            } catch (e: Exception) {
                _featuredMovies.value = MovieState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getAllMovies() {
        viewModelScope.launch {
            _allMovies.value = MovieState.Loading
            try {
                val response = repository.getAllMovies()
                if (response.isSuccessful) {
                    _allMovies.value = MovieState.Success((response.body() ?: emptyList()) as List<Movie>)
                } else {
                    _allMovies.value = MovieState.Error("Failed to load movies")
                }
            } catch (e: Exception) {
                _allMovies.value = MovieState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}

sealed class MovieState {
    object Idle : MovieState()
    object Loading : MovieState()
    data class Success(val movies: List<Movie>) : MovieState()
    data class Error(val message: String) : MovieState()
}
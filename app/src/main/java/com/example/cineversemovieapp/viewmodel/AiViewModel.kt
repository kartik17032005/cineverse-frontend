package com.example.cineversemovieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineversemovieapp.models.AiMovieResult
import com.example.cineversemovieapp.models.AiRecommendation
import com.example.cineversemovieapp.network.Auth.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AiViewModel : ViewModel() {

    private val api = RetrofitInstance.api

    private val _aiState = MutableStateFlow<AiState>(AiState.Idle)
    val aiState: StateFlow<AiState> = _aiState

    fun getRecommendations(query: String) {
        // don't search empty queries
        if (query.isBlank()) return

        viewModelScope.launch {
            _aiState.value = AiState.Loading

            try {
                val response = api.getAiRecommendations(
                    AiRecommendation(query = query)
                )

                if (response.isSuccessful) {
                    val movies = response.body() ?: emptyList()
                    _aiState.value = AiState.Success(movies)
                } else {
                    _aiState.value = AiState.Error("Failed to get recommendations")
                }

            } catch (e: Exception) {
                _aiState.value = AiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun reset() {
        _aiState.value = AiState.Idle
    }
}

sealed class AiState {
    object Idle : AiState()
    object Loading : AiState()
    data class Success(val movies: List<AiMovieResult>) : AiState()
    data class Error(val message: String) : AiState()
}
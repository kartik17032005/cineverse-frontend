package com.example.cineversemovieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineversemovieapp.data.tmdb.TmdbCastMember
import com.example.cineversemovieapp.data.tmdb.TmdbMovie
import com.example.cineversemovieapp.data.tmdb.TmdbMovieDetail
import com.example.cineversemovieapp.data.tmdb.TmdbTvShow
import com.example.cineversemovieapp.data.tmdb.toTmdbMovie
import com.example.cineversemovieapp.models.TmdbVideo
import com.example.cineversemovieapp.repository.TmdbRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TmdbViewModel : ViewModel() {

    private val repository = TmdbRepository()

    private val _trendingMovies = MutableStateFlow<TmdbState>(TmdbState.Idle)
    val trendingMovies: StateFlow<TmdbState> = _trendingMovies

    private val _topRatedMovies = MutableStateFlow<TmdbState>(TmdbState.Idle)
    val topRatedMovies: StateFlow<TmdbState> = _topRatedMovies

    private val _searchResults = MutableStateFlow<TmdbState>(TmdbState.Idle)
    val searchResults: StateFlow<TmdbState> = _searchResults

    private val _bollywoodMovies = MutableStateFlow<TmdbState>(TmdbState.Idle)
    val bollywoodMovies: StateFlow<TmdbState> = _bollywoodMovies

    private val _animeShows = MutableStateFlow<AnimeState>(AnimeState.Idle)
    val animeShows: StateFlow<AnimeState> = _animeShows

    private val _hollywoodClassics = MutableStateFlow<TmdbState>(TmdbState.Idle)
    val hollywoodClassics: StateFlow<TmdbState> = _hollywoodClassics

    private val _kDramas = MutableStateFlow<AnimeState>(AnimeState.Idle)
    val kDramas: StateFlow<AnimeState> = _kDramas

    private val _awardWinners = MutableStateFlow<TmdbState>(TmdbState.Idle)
    val awardWinners: StateFlow<TmdbState> = _awardWinners

    private val _nowPlaying = MutableStateFlow<TmdbState>(TmdbState.Idle)
    val nowPlaying: StateFlow<TmdbState> = _nowPlaying

    private val _popularTvShows = MutableStateFlow<AnimeState>(AnimeState.Idle)
    val popularTvShows: StateFlow<AnimeState> = _popularTvShows

    private val _upcomingMovies = MutableStateFlow<TmdbState>(TmdbState.Idle)
    val upcomingMovies: StateFlow<TmdbState> = _upcomingMovies

    private val _movieCredits = MutableStateFlow<CreditsState>(CreditsState.Idle)
    val movieCredits: StateFlow<CreditsState> = _movieCredits

    private val _reelsMovies = MutableStateFlow<TmdbState>(TmdbState.Idle)
    val reelsMovies: StateFlow<TmdbState> = _reelsMovies

    private val _recommendations = MutableStateFlow<TmdbState>(TmdbState.Idle)
    val recommendations: StateFlow<TmdbState> = _recommendations

    fun getTrendingMovies() {
        viewModelScope.launch {
            _trendingMovies.value = TmdbState.Loading
            try {
                val response = repository.getTrendingMovies()
                if (response.isSuccessful) {
                    _trendingMovies.value = TmdbState.Success(response.body()?.results ?: emptyList())
                } else {
                    _trendingMovies.value = TmdbState.Error("Failed to load trending")
                }
            } catch (e: Exception) {
                _trendingMovies.value = TmdbState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getTopRatedMovies() {
        viewModelScope.launch {
            _topRatedMovies.value = TmdbState.Loading
            try {
                val response = repository.getTopRatedMovies()
                if (response.isSuccessful) {
                    _topRatedMovies.value = TmdbState.Success(response.body()?.results ?: emptyList())
                } else {
                    _topRatedMovies.value = TmdbState.Error("Failed to load top rated")
                }
            } catch (e: Exception) {
                _topRatedMovies.value = TmdbState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _searchResults.value = TmdbState.Loading
            try {
                val response = repository.searchMovies(query)
                if (response.isSuccessful) {
                    _searchResults.value = TmdbState.Success(response.body()?.results ?: emptyList())
                } else {
                    _searchResults.value = TmdbState.Error("Search failed")
                }
            } catch (e: Exception) {
                _searchResults.value = TmdbState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun searchMoviesByGenre(genreId: Int) {
        viewModelScope.launch {
            _searchResults.value = TmdbState.Loading
            try {
                val response = repository.getMoviesByGenre(genreId)
                if (response.isSuccessful) {
                    _searchResults.value = TmdbState.Success(response.body()?.results ?: emptyList())
                } else {
                    _searchResults.value = TmdbState.Error("Failed to load movies for genre")
                }
            } catch (e: Exception) {
                _searchResults.value = TmdbState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun resetSearchResults() {
        _searchResults.value = TmdbState.Idle
    }

    fun getBollywoodMovies() {
        viewModelScope.launch {
            _bollywoodMovies.value = TmdbState.Loading
            try {
                val response = repository.getBollywoodMovies()
                if (response.isSuccessful) {
                    _bollywoodMovies.value = TmdbState.Success(response.body()?.results ?: emptyList())
                } else {
                    _bollywoodMovies.value = TmdbState.Error("Failed to load bollywood movies")
                }
            } catch (e: Exception) {
                _bollywoodMovies.value = TmdbState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getAnimeShows() {
        viewModelScope.launch {
            _animeShows.value = AnimeState.Loading
            try {
                val response = repository.getAnimeShows()
                if (response.isSuccessful) {
                    _animeShows.value = AnimeState.Success(response.body()?.results ?: emptyList())
                } else {
                    _animeShows.value = AnimeState.Error("Failed to load anime")
                }
            } catch (e: Exception) {
                _animeShows.value = AnimeState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getHollywoodClassics() {
        viewModelScope.launch {
            _hollywoodClassics.value = TmdbState.Loading
            try {
                val response = repository.getHollywoodClassics()
                if (response.isSuccessful) {
                    _hollywoodClassics.value = TmdbState.Success(response.body()?.results ?: emptyList())
                } else {
                    _hollywoodClassics.value = TmdbState.Error("Failed to load classics")
                }
            } catch (e: Exception) {
                _hollywoodClassics.value = TmdbState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getKDramas() {
        viewModelScope.launch {
            _kDramas.value = AnimeState.Loading
            try {
                val response = repository.getKDramas()
                if (response.isSuccessful) {
                    _kDramas.value = AnimeState.Success(response.body()?.results ?: emptyList())
                } else {
                    _kDramas.value = AnimeState.Error("Failed to load K-Dramas")
                }
            } catch (e: Exception) {
                _kDramas.value = AnimeState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getAwardWinners() {
        viewModelScope.launch {
            _awardWinners.value = TmdbState.Loading
            try {
                val response = repository.getAwardWinners()
                if (response.isSuccessful) {
                    _awardWinners.value = TmdbState.Success(response.body()?.results ?: emptyList())
                } else {
                    _awardWinners.value = TmdbState.Error("Failed to load award winners")
                }
            } catch (e: Exception) {
                _awardWinners.value = TmdbState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getNowPlaying() {
        viewModelScope.launch {
            _nowPlaying.value = TmdbState.Loading
            try {
                val response = repository.getNowPlaying()
                if (response.isSuccessful) {
                    _nowPlaying.value = TmdbState.Success(response.body()?.results ?: emptyList())
                } else {
                    _nowPlaying.value = TmdbState.Error("Failed to load now playing")
                }
            } catch (e: Exception) {
                _nowPlaying.value = TmdbState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getPopularTvShows() {
        viewModelScope.launch {
            _popularTvShows.value = AnimeState.Loading
            try {
                val response = repository.getPopularTvShows()
                if (response.isSuccessful) {
                    _popularTvShows.value = AnimeState.Success(response.body()?.results ?: emptyList())
                } else {
                    _popularTvShows.value = AnimeState.Error("Failed to load TV shows")
                }
            } catch (e: Exception) {
                _popularTvShows.value = AnimeState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getUpcomingMovies() {
        viewModelScope.launch {
            _upcomingMovies.value = TmdbState.Loading
            try {
                val response = repository.getUpcomingMovies()
                if (response.isSuccessful) {
                    _upcomingMovies.value = TmdbState.Success(response.body()?.results ?: emptyList())
                } else {
                    _upcomingMovies.value = TmdbState.Error("Failed to load upcoming")
                }
            } catch (e: Exception) {
                _upcomingMovies.value = TmdbState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    private val _movieDetail = MutableStateFlow<MovieDetailState>(MovieDetailState.Idle)
    val movieDetail: StateFlow<MovieDetailState> = _movieDetail

    fun getMovieDetails(id: Int, isTv: Boolean = false) {
        viewModelScope.launch {
            _movieDetail.value = MovieDetailState.Loading
            try {
                val response = if (isTv) {
                    repository.getTvDetails(id)
                } else {
                    repository.getMovieDetails(id)
                }
                
                if (response.isSuccessful && response.body() != null) {
                    _movieDetail.value = MovieDetailState.Success(response.body()!!)
                } else {
                    val errorLabel = if (isTv) "TV Show" else "Movie"
                    _movieDetail.value = MovieDetailState.Error("Failed to load $errorLabel details (Code: ${response.code()})")
                }
            } catch (e: Exception) {
                _movieDetail.value = MovieDetailState.Error(e.message ?: "Network error occurred")
            }
        }
    }

    fun getMovieCredits(id: Int, isTv: Boolean = false) {
        viewModelScope.launch {
            _movieCredits.value = CreditsState.Loading
            try {
                val response = if (isTv) {
                    repository.getTvCredits(id)
                } else {
                    repository.getMovieCredits(id)
                }
                
                if (response.isSuccessful) {
                    _movieCredits.value = CreditsState.Success(response.body()?.cast?.take(10) ?: emptyList())
                } else {
                    _movieCredits.value = CreditsState.Error("Failed to load credits")
                }
            } catch (e: Exception) {
                _movieCredits.value = CreditsState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    private val _movieVideos = MutableStateFlow<VideosState>(VideosState.Idle)
    val movieVideos: StateFlow<VideosState> = _movieVideos
    
    private val _reelsVideoKeys = MutableStateFlow<Map<Int, String>>(emptyMap())
    val reelsVideoKeys = _reelsVideoKeys.asStateFlow()

    fun getMovieVideos(id: Int, isTv: Boolean = false) {
        viewModelScope.launch {
            _movieVideos.value = VideosState.Loading
            try {
                val response = if (isTv) {
                    repository.getTvVideos(id)
                } else {
                    repository.getMovieVideos(id)
                }
                
                if (response.isSuccessful) {
                    val trailers = response.body()?.results
                        ?.filter { it.site == "YouTube" && (it.type == "Trailer" || it.type == "Opening Credits" || it.type == "Teaser") }
                        ?: emptyList()
                    
                    val key = trailers.firstOrNull()?.key
                    if (key != null) {
                        _reelsVideoKeys.value += (id to key)
                    }
                    
                    _movieVideos.value = VideosState.Success(trailers)
                } else {
                    _movieVideos.value = VideosState.Error("Failed to load trailers")
                }
            } catch (e: Exception) {
                _movieVideos.value = VideosState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getReelsMovies(mood: String = "NEUTRAL") {
        val genreId = when (mood) {
            "HAPPY"   -> 35   
            "SAD"     -> 18   
            "SLEEPY"  -> 12   
            "EXCITED" -> 28   
            "NEUTRAL" -> 878  
            else      -> 28   
        }

        viewModelScope.launch {
            _reelsMovies.value = TmdbState.Loading
            try {
                val response = repository.getMoviesByGenre(genreId)
                if (response.isSuccessful) {
                    _reelsMovies.value = TmdbState.Success(response.body()?.results ?: emptyList())
                } else {
                    _reelsMovies.value = TmdbState.Error("Failed to load reels")
                }
            } catch (e: Exception) {
                _reelsMovies.value = TmdbState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getRecommendations(id: Int, isTv: Boolean = false) {
        viewModelScope.launch {
            _recommendations.value = TmdbState.Loading
            try {
                if (isTv) {
                    val response = repository.getTvRecommendations(id)
                    if (response.isSuccessful) {
                        val movies = response.body()?.results?.map { it.toTmdbMovie() } ?: emptyList()
                        _recommendations.value = TmdbState.Success(movies)
                    } else {
                        _recommendations.value = TmdbState.Error("Failed to load recommendations")
                    }
                } else {
                    val response = repository.getMovieRecommendations(id)
                    if (response.isSuccessful) {
                        _recommendations.value = TmdbState.Success(response.body()?.results ?: emptyList())
                    } else {
                        _recommendations.value = TmdbState.Error("Failed to load recommendations")
                    }
                }
            } catch (e: Exception) {
                _recommendations.value = TmdbState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun resetMovieVideos() {
        _movieVideos.value = VideosState.Idle
    }

    sealed class VideosState {
        object Idle : VideosState()
        object Loading : VideosState()
        data class Success(val videos: List<TmdbVideo>) : VideosState()
        data class Error(val message: String) : VideosState()
    }

    fun resetMovieDetail() {
        _movieDetail.value = MovieDetailState.Idle
    }

    fun resetMovieCredits() {
        _movieCredits.value = CreditsState.Idle
    }

    fun resetRecommendations() {
        _recommendations.value = TmdbState.Idle
    }
}

sealed class TmdbState {
    object Idle : TmdbState()
    object Loading : TmdbState()
    data class Success(val movies: List<TmdbMovie>) : TmdbState()
    data class Error(val message: String) : TmdbState()
}

sealed class AnimeState {
    object Idle : AnimeState()
    object Loading : AnimeState()
    data class Success(val shows: List<TmdbTvShow>) : AnimeState()
    data class Error(val message: String) : AnimeState()
}

sealed class MovieDetailState {
    object Idle : MovieDetailState()
    object Loading : MovieDetailState()
    data class Success(val movie: TmdbMovieDetail) : MovieDetailState()
    data class Error(val message: String) : MovieDetailState()
}

sealed class CreditsState {
    object Idle : CreditsState()
    object Loading : CreditsState()
    data class Success(val cast: List<TmdbCastMember>) : CreditsState()
    data class Error(val message: String) : CreditsState()
}

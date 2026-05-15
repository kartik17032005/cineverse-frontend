package com.example.cineversemovieapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineversemovieapp.models.AddWatchlistRequest
import com.example.cineversemovieapp.models.LoginRequest
import com.example.cineversemovieapp.models.RegisterRequest
import com.example.cineversemovieapp.models.UpdateUserRequest
import com.example.cineversemovieapp.models.UserResponse
import com.example.cineversemovieapp.models.WatchlistItem
import com.example.cineversemovieapp.network.Auth.RetrofitInstance
import com.example.cineversemovieapp.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    // ── Splash/Auth check ──
    private val _isCheckingAuth = MutableStateFlow(true)
    val isCheckingAuth: StateFlow<Boolean> = _isCheckingAuth

    init {
        viewModelScope.launch {
            // Simulate a short delay for auth check
            delay(1000)
            _isCheckingAuth.value = false
        }
    }

    // ── current logged in user id ──
    private var currentUserId: Long = 0L       // 👈 NOT in companion object

    fun setCurrentUserId(id: Long) {
        currentUserId = id
        Log.d("AUTH", "currentUserId set to: $id")
    }

    // ── Register ──
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun registerUser(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val response = repository.registerUser(
                    RegisterRequest(
                        userName = username,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword
                    )
                )
                if (response.isSuccessful) {
                    _registerState.value = RegisterState.Success
                } else {
                    _registerState.value = RegisterState.Error("Registration failed. Try again.")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun resetRegisterState() {
        _registerState.value = RegisterState.Idle
    }

    // ── Login ──
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    // store token after login/register
    private var jwtToken: String = ""

    fun getToken() = jwtToken

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = repository.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val authResponse = response.body()!!
                    jwtToken = authResponse.token
                    RetrofitInstance.token = authResponse.token  // 👈 set globally
                    setCurrentUserId(authResponse.userId)
                    _loginState.value = LoginState.Success(authResponse.userId)
                } else {
                    _loginState.value = LoginState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    // ── User Profile ──
    private val _userProfile = MutableStateFlow<UserProfileState>(UserProfileState.Idle)
    val userProfile: StateFlow<UserProfileState> = _userProfile

    fun getUserProfile() {
        Log.d("AUTH", "fetching profile for userId: $currentUserId")
        viewModelScope.launch {
            _userProfile.value = UserProfileState.Loading
            try {
                val response = repository.getUserById(currentUserId)
                if (response.isSuccessful) {
                    _userProfile.value = UserProfileState.Success(response.body()!!)
                } else {
                    _userProfile.value = UserProfileState.Error("Failed to load profile")
                }
            } catch (e: Exception) {
                _userProfile.value = UserProfileState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    // ── Update User ──
    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState

    fun updateUser(userName: String, email: String) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            try {
                val response = repository.updateUser(
                    currentUserId,
                    UpdateUserRequest(userName = userName, email = email)
                )
                if (response.isSuccessful) {
                    _updateState.value = UpdateState.Success
                    getUserProfile()    // refresh profile after update
                } else {
                    _updateState.value = UpdateState.Error("Update failed")
                }
            } catch (e: Exception) {
                _updateState.value = UpdateState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = UpdateState.Idle
    }

    // ── States ──
    sealed class UserProfileState {
        object Idle : UserProfileState()
        object Loading : UserProfileState()
        data class Success(val user: UserResponse) : UserProfileState()
        data class Error(val message: String) : UserProfileState()
    }

    sealed class UpdateState {
        object Idle : UpdateState()
        object Loading : UpdateState()
        object Success : UpdateState()
        data class Error(val message: String) : UpdateState()
    }

    // ── Watchlist States ──
    private val _watchlistState = MutableStateFlow<WatchlistState>(WatchlistState.Idle)
    val watchlistState: StateFlow<WatchlistState> = _watchlistState

    private val _watchlistIds = MutableStateFlow<Set<Long>>(emptySet())
    val watchlistIds: StateFlow<Set<Long>> = _watchlistIds

    private val _watchlistActionState = MutableStateFlow<WatchlistActionState>(WatchlistActionState.Idle)
    val watchlistActionState: StateFlow<WatchlistActionState> = _watchlistActionState

    // ── Get Watchlist ──
    fun getWatchlist() {
        viewModelScope.launch {
            _watchlistState.value = WatchlistState.Loading
            try {
                val response = repository.getWatchlist(currentUserId)
                if (response.isSuccessful) {
                    val items = response.body() ?: emptyList()
                    _watchlistState.value = WatchlistState.Success(items)
                    // store just the IDs for quick bookmark checking
                    _watchlistIds.value = items.map { it.tmdbId }.toSet()
                } else {
                    _watchlistState.value = WatchlistState.Error("Failed to load watchlist")
                }
            } catch (e: Exception) {
                _watchlistState.value = WatchlistState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    // ── Add to Watchlist ──
    fun addToWatchlist(
        tmdbId: Long,
        title: String?,
        posterUrl: String,
        backdropUrl: String,
        rating: String,
        releaseYear: String,
        genre: String
    ) {
        Log.d("WATCHLIST", "Adding movie: $title, userId: $currentUserId, tmdbId: $tmdbId")
        viewModelScope.launch {
            _watchlistActionState.value = WatchlistActionState.Loading
            try {
                val response = repository.addToWatchlist(
                    currentUserId,
                    AddWatchlistRequest(
                        tmdbId = tmdbId,
                        movieTitle = title,
                        posterUrl = posterUrl,
                        backdropUrl = backdropUrl,
                        rating = rating,
                        releaseYear = releaseYear,
                        genre = genre
                    )
                )
                if (response.isSuccessful) {
                    _watchlistActionState.value = WatchlistActionState.Added
                    // update local ids set
                    _watchlistIds.value = _watchlistIds.value + tmdbId
                    getWatchlist() // refresh list
                } else {
                    _watchlistActionState.value = WatchlistActionState.Error("Already in watchlist")
                }
            } catch (e: Exception) {
                _watchlistActionState.value = WatchlistActionState.Error(e.message ?: "Failed")
            }
        }
    }

    // ── Remove from Watchlist ──
    fun removeFromWatchlist(tmdbId: Long) {
        viewModelScope.launch {
            _watchlistActionState.value = WatchlistActionState.Loading
            try {
                val response = repository.removeFromWatchlist(currentUserId, tmdbId)
                if (response.isSuccessful) {
                    _watchlistActionState.value = WatchlistActionState.Removed
                    // update local ids set
                    _watchlistIds.value = _watchlistIds.value - tmdbId
                    getWatchlist() // refresh list
                } else {
                    _watchlistActionState.value = WatchlistActionState.Error("Failed to remove")
                }
            } catch (e: Exception) {
                _watchlistActionState.value = WatchlistActionState.Error(e.message ?: "Failed")
            }
        }
    }

    fun resetWatchlistAction() {
        _watchlistActionState.value = WatchlistActionState.Idle
    }

    // ── Watchlist States ──
    sealed class WatchlistState {
        object Idle : WatchlistState()
        object Loading : WatchlistState()
        data class Success(val items: List<WatchlistItem>) : WatchlistState()
        data class Error(val message: String) : WatchlistState()
    }

    sealed class WatchlistActionState {
        object Idle : WatchlistActionState()
        object Loading : WatchlistActionState()
        object Added : WatchlistActionState()
        object Removed : WatchlistActionState()
        data class Error(val message: String) : WatchlistActionState()
    }
}

// ── Login State ──
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userId: Long) : LoginState()  // 👈 fixed - has userId
    data class Error(val message: String) : LoginState()
}

// ── Register State ──
sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()                     // 👈 fixed - extends RegisterState
    data class Error(val message: String) : RegisterState()
}

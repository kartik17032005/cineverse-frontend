package com.example.cineversemovieapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cineversemovieapp.data.badge.BadgeRepository
import com.example.cineversemovieapp.data.badge.CineBadge
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BadgeViewModel : ViewModel() {
    private val _badges = MutableStateFlow<List<CineBadge>>(emptyList())
    val badges: StateFlow<List<CineBadge>> = _badges.asStateFlow()

    init {
        loadBadges()
    }

    private fun loadBadges() {
        _badges.value = BadgeRepository.getBadges()
    }
}

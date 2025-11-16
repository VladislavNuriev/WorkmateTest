package com.example.profile.ui

import com.example.domain.model.User

sealed interface UserProfileScreenState {
    data object Initial : UserProfileScreenState

    data class UserProfile(
        val user: User
    ): UserProfileScreenState
}
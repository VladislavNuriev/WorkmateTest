package com.example.profile

import com.example.domain.model.User

sealed interface UserProfileScreenState {
    data object Initial : UserProfileScreenState

    data class UserProfile(
        val user: User
    ): UserProfileScreenState
}



package com.example.users

import com.example.domain.model.User

data class UsersScreenState(
    val users: List<User> = emptyList(),
    val searchQuery: String = "",
)

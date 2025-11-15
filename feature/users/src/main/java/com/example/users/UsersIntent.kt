package com.example.users

sealed interface UsersIntent {
    data class SearchQueryChanged(val query: String) : UsersIntent
    data class DeleteUser(val id: Int, val imageFilePath: String) : UsersIntent
}
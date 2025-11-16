package com.example.generateuser.ui

sealed interface GenerateUserScreenState {
    data class Generate(
        val isLoading: Boolean = false,
        val gender: String = "",
        val nationality: String = "",
        val error: String? = null
    ) : GenerateUserScreenState {
        val isSaveEnabled: Boolean
            get() = gender.isNotBlank() && nationality.isNotBlank() && !isLoading
    }
}
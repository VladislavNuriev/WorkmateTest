package com.example.generateuser.ui

sealed interface GenerateUserIntent {
    data class GenderSelected(val gender: String) : GenerateUserIntent
    data class NationalitySelected(val nationality: String) : GenerateUserIntent
    data object GenerateUser : GenerateUserIntent
}
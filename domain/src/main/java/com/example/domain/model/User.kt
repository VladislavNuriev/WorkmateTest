package com.example.domain.model

data class User(
    val id: Int,
    val gender: String,
    val firstName: String,
    val lastName: String,
    val title: String,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val email: String,
    val birthDate: String,
    val age: Int,
    val phone: String,
    val cell: String,
    val nationality: String,
    val pictureFilePath: String
)
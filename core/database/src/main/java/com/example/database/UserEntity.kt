package com.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int = 0,
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

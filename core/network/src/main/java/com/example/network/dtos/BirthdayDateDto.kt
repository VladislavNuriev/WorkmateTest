package com.example.network.dtos

import com.google.gson.annotations.SerializedName

data class BirthdayDateDto(
    @SerializedName("date")
    val date: String,

    @SerializedName("age")
    val age: Int
)

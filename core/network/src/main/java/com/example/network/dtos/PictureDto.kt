package com.example.network.dtos

import com.google.gson.annotations.SerializedName

data class PictureDto(
    @SerializedName("large")
    val large: String,
)

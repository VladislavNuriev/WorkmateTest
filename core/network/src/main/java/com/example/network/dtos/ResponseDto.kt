package com.example.network.dtos

import com.google.gson.annotations.SerializedName

data class ResponseDto(
    @SerializedName("results")
    val results: List<UserDto>
)


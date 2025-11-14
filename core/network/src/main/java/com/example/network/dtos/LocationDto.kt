package com.example.network.dtos

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("city")
    val city: String,

    @SerializedName("state")
    val state: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("postcode")
    val postcode: String,
)

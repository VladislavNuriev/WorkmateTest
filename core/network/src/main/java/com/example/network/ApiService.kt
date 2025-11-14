package com.example.network

import com.example.network.dtos.ResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/")
    suspend fun getUser(
        @Query("gender") gender: String,
        @Query("nat") nat: String
    ): ResponseDto
}
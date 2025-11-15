package com.example.domain

import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun saveRandomUser(gender: String, nationality: String): Result<Unit>

    suspend fun getSavedUsers(query: String): Flow<List<User>>

    suspend fun getUserById(userId: Int): User

    suspend fun deleteUser(userId: Int, imageFilePath: String): Result<Unit>
}
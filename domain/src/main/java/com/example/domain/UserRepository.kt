package com.example.domain

import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun saveRandomUser(): Result<Unit>

    suspend fun getSavedUsers(): Flow<List<User>>

    suspend fun getUserById(userId: Int): Flow<User>

    suspend fun deleteUser(userId: Int): Result<Unit>
}
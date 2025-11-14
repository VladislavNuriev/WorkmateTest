package com.example.domain.usecases

import com.example.domain.UserRepository
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedUsersUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(): Flow<List<User>> = repository.getSavedUsers()
}
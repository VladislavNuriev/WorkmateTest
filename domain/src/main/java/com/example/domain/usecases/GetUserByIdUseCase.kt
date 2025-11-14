package com.example.domain.usecases

import com.example.domain.UserRepository
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserByIdUseCase@Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userId: Int): Flow<User> = repository.getUserById(userId)
}
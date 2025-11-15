package com.example.domain.usecases

import com.example.domain.UserRepository
import com.example.domain.model.User
import javax.inject.Inject

class GetUserByIdUseCase@Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userId: Int): User = repository.getUserById(userId)
}
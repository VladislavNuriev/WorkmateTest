package com.example.domain.usecases

import com.example.domain.UserRepository
import javax.inject.Inject

class DeleteUserUseCase@Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userId: Int): Result<Unit> = repository.deleteUser(userId)
}
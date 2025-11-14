package com.example.domain.usecases

import com.example.domain.UserRepository
import javax.inject.Inject

class SaveRandomUserUseCase@Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.saveRandomUser()
}
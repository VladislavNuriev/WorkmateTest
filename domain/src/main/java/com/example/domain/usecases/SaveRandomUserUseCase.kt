package com.example.domain.usecases

import com.example.domain.UserRepository
import javax.inject.Inject

class SaveRandomUserUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(gender: String, nationality: String): Result<Unit> =
        repository.saveRandomUser(gender, nationality)
}
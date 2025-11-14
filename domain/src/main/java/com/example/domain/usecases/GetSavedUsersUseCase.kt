package com.example.domain.usecases

import com.example.domain.UserRepository
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.management.Query

class GetSavedUsersUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(query: String): Flow<List<User>> = repository.getSavedUsers(query)
}
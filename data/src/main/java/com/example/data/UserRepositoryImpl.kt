package com.example.data

import com.example.database.UserDao
import com.example.domain.UserRepository
import com.example.domain.model.User
import com.example.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val imageFileManager: ImageFileManager,
) : UserRepository {

    override suspend fun getUserById(userId: Int): User {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(userId).toDomain()
        }
    }

    override suspend fun saveRandomUser(gender: String, nationality: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val userDto = apiService.getUser(gender = gender, nat = nationality)
                    .results
                    .firstOrNull()
                    ?: throw IllegalStateException("No user in response")

                val picturePath = imageFileManager.downloadAndSaveImage(
                    userDto.picture.large
                )
                val user = userDto.toEntity(picturePath)
                userDao.insertUser(user)
            }.onFailure {
                Result.failure<Exception>(it)
            }
        }
    }

    override suspend fun getSavedUsers(query: String): Flow<List<User>> {
        return withContext(Dispatchers.IO) {
            userDao.getUsers(query).map {
                it.toUsers()
            }
        }
    }

    override suspend fun deleteUser(userId: Int, imageFilePath: String): Result<Unit> {
        return runCatching {
            userDao.deleteUser(userId)
            imageFileManager.deleteImage(imageFilePath)
        }
    }
}

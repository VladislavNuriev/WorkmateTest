package com.example.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(entity = UserEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query(
        """
    SELECT * FROM users 
    WHERE
            firstName LIKE '%' || :searchQuery || '%' 
            OR lastName LIKE '%' || :searchQuery || '%'
    """
    )
    fun getUsers(searchQuery: String = ""): Flow<List<UserEntity>>

    @Query(
        "SELECT * FROM users WHERE id = :userId"
    )
    fun getUserById(userId: Int): UserEntity

    @Query("DELETE FROM users WHERE id == :userId")
    suspend fun deleteUser(userId: Int)
}

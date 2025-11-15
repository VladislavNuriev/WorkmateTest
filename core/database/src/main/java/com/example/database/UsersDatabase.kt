package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class],
    version = 3,
    exportSchema = false
)
abstract class UsersDatabase : RoomDatabase() {

    abstract fun usersDao(): UserDao

    companion object {
        private var instance: UsersDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): UsersDatabase {
            instance?.let {
                return it
            }
            synchronized(LOCK) {
                instance?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    context,
                    UsersDatabase::class.java,
                    "notes.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                instance = db
                return db
            }
        }
    }
}

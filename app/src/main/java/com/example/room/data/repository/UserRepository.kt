package com.example.room.data.repository

import androidx.lifecycle.LiveData
import com.example.room.data.database.UserDao
import com.example.room.data.model.User

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }
}
package com.nammaraste.health.data

class AuthRepository(private val userDao: UserDao) {
    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun registerUser(user: UserEntity) {
        userDao.insertUser(user)
    }
}

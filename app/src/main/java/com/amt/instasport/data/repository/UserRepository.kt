package com.amt.instasport.data.repository

import com.amt.instasport.data.model.User
import com.amt.instasport.managers.UserDatabaseManager

// Abstracts the source of user data
// Uses UserDatabaseManager to fetch user data from Firebase
class UserRepository(private val userDatabaseManager: UserDatabaseManager) {
    suspend fun getUser(userId: String): User? {
        return userDatabaseManager.getUserDataFromDatabase(userId)
    }

    suspend fun uploadUserData(user: User) {
        userDatabaseManager.uploadUserDataToDatabase(user)
    }

}

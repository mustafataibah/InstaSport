package com.amt.instasport.repository

import com.amt.instasport.manager.UserDatabaseManager
import com.amt.instasport.model.User

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

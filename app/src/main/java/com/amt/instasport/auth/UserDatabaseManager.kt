package com.amt.instasport.auth

import com.amt.instasport.data.model.User
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

// Low level direct db operations
class UserDatabaseManager(private val firebaseDatabase: FirebaseDatabase) {

    suspend fun uploadUserDataToDatabase(user: User) {
        try {
            firebaseDatabase.reference.child("users").child(user.uid)
                .setValue(user).await()
            // Handle success
        } catch (e: Exception) {
            // Handle failure
        }
    }
}

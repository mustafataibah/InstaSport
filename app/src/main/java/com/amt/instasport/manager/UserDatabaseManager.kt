package com.amt.instasport.manager

import android.util.Log
import com.amt.instasport.model.User
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

// Directly interacts with Firebase to perform CRUD operations
class UserDatabaseManager(private val firebaseDatabase: FirebaseDatabase) {
    suspend fun uploadUserDataToDatabase(user: User) {
        try {
            firebaseDatabase.reference.child("users").child(user.uid).setValue(user).await()
        } catch (_: Exception) {
        }
    }

    suspend fun getUserDataFromDatabase(userId: String): User? {
        return try {
            val dataSnapshot = firebaseDatabase.reference.child("users").child(userId).get().await()
            Log.d("UserDatabaseManager", "Raw DataSnapshot: $dataSnapshot")
            dataSnapshot.getValue(User::class.java) ?: throw Exception("Failed to deserialize data")
        } catch (e: Exception) {
            Log.e("UserDatabaseManager", "Error fetching user data", e)
            null
        }
    }
}

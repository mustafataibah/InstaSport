package com.amt.instasport.manager

import android.util.Log
import com.amt.instasport.model.Event
import com.amt.instasport.model.User
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class EventsDatabaseManager(private val firebaseDatabase: FirebaseDatabase) {
    suspend fun uploadEventsDataToDatabase(event: Event) {
        try {
            firebaseDatabase.reference.child("events").child(event.eventId)
                .setValue(event).await()
        } catch (e: Exception) {
        }
    }

    suspend fun getEventDataFromDatabase(eventId: String): Event? {
        return try {
            val dataSnapshot = firebaseDatabase.reference.child("events").child(eventId).get().await()
            Log.d("EventDatabaseManager", "Raw DataSnapshot: $dataSnapshot")
            dataSnapshot.getValue(Event::class.java) ?: throw Exception("Failed to deserialize data")
        } catch (e: Exception) {
            Log.e("EventDatabaseManager", "Error fetching user data", e)
            null
        }
    }
}

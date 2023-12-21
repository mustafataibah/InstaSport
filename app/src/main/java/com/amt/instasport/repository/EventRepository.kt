package com.amt.instasport.repository

import com.amt.instasport.manager.EventsDatabaseManager
import com.amt.instasport.model.Event

class EventRepository(private val eventDatabaseManager: EventsDatabaseManager) {
//    suspend fun getEvent(eventId: String): Event? {
//        return eventDatabaseManager.getEventDataFromDatabase(eventId)
//    }

    suspend fun uploadEventData(event: Event) {
        eventDatabaseManager.uploadEventsDataToDatabase(event)
    }

    suspend fun getAllEvents(): List<Event> {
        return eventDatabaseManager.getAllEvents()
    }
}
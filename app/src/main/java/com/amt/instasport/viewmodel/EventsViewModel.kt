package com.amt.instasport.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amt.instasport.model.Event
import com.amt.instasport.repository.EventRepository
import kotlinx.coroutines.launch

// GPT
class EventsViewModelFactory(private val eventRepository: EventRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventsViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class EventsViewModel(private val eventRepository: EventRepository) : ViewModel() {
    val currentEvents = MutableLiveData<Event?>()
    val allEvents = MutableLiveData<List<Event>>()

    fun uploadEventsData(event: Event) {
        viewModelScope.launch {
            eventRepository.uploadEventData(event)
            currentEvents.value = event
        }
    }

    fun fetchEvents(eventId: String) {
        viewModelScope.launch {
            val events = eventRepository.getEvent(eventId)
            currentEvents.value = events
        }
    }

    fun fetchAllEvents() {
        viewModelScope.launch {
            val events = eventRepository.getAllEvents()
            allEvents.value = events
        }
    }
}

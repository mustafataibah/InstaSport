package com.amt.instasport.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amt.instasport.manager.EventsDatabaseManager
import com.amt.instasport.viewmodel.EventsViewModel
import com.amt.instasport.viewmodel.UserDataViewModel

// Derived from GPT code
class EventsViewModelFactory(private val eventsDatabaseManager: EventsDatabaseManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventsViewModel(eventsDatabaseManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

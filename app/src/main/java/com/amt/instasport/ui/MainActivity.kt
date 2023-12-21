package com.amt.instasport.ui

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.amt.instasport.manager.AuthenticationManager
import com.amt.instasport.manager.EventsDatabaseManager
import com.amt.instasport.manager.UserDatabaseManager
import com.amt.instasport.repository.EventRepository
import com.amt.instasport.repository.UserRepository
import com.amt.instasport.ui.theme.InstaSportTheme
import com.amt.instasport.viewmodel.AuthViewModel
import com.amt.instasport.viewmodel.AuthViewModelFactory
import com.amt.instasport.viewmodel.EventsViewModel
import com.amt.instasport.viewmodel.EventsViewModelFactory
import com.amt.instasport.viewmodel.UserDataViewModel
import com.amt.instasport.viewmodel.UserDataViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Instantiate ViewModels here
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val userDatabaseManager = UserDatabaseManager(firebaseDatabase)
        val userRepository = UserRepository(userDatabaseManager)
        val eventsDatabaseManager = EventsDatabaseManager(firebaseDatabase)
        val eventsRepository = EventRepository(eventsDatabaseManager)


        // AuthViewModel
        val authViewModel: AuthViewModel by viewModels {
            AuthViewModelFactory(
                AuthenticationManager(firebaseAuth)
            )
        }

        // UserDataViewModel
        val userDataViewModel: UserDataViewModel by viewModels {
            UserDataViewModelFactory(
                userRepository
            )
        }

        //EventsViewModel
        val eventsViewModel: EventsViewModel by viewModels {
            EventsViewModelFactory(
                eventsRepository
            )
        }

        setContent {
            InstaSportTheme {
                AppNavigation(authViewModel, userDataViewModel, eventsViewModel)
            }
        }
    }
}
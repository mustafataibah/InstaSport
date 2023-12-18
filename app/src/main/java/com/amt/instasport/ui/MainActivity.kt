package com.amt.instasport.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.amt.instasport.manager.AuthenticationManager
import com.amt.instasport.manager.UserDatabaseManager
import com.amt.instasport.repository.UserRepository
import com.amt.instasport.ui.theme.InstaSportTheme
import com.amt.instasport.viewmodel.AuthViewModel
import com.amt.instasport.viewmodel.AuthViewModelFactory
import com.amt.instasport.viewmodel.UserDataViewModel
import com.amt.instasport.viewmodel.UserDataViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instantiate ViewModels here
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val userDatabaseManager = UserDatabaseManager(firebaseDatabase)
        val userRepository = UserRepository(userDatabaseManager)
//        val eventsDatabaseManager = EventsDatabaseManager(firebaseDatabase)


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

        // EventsViewModel
//        val eventsViewModel: EventsViewModel by viewModels {
//            EventsViewModelFactory(
//                eventsDatabaseManager
//            )
//        }

        // TODO: fetchUserData and fetchEventsData methods to get all the users data if logged in
//        if (firebaseAuth.currentUser != null) {
//            // User is already logged in
//            userDataViewModel.fetchUserData(firebaseAuth.currentUser!!.uid)
////            eventsViewModel.fetchEventsData()
//        }

        setContent {
            InstaSportTheme {
                // Then Pass ViewModels to AppNavigation (so I believe that the lifecycle of the view models are based on the Main activities lifecycle)
                AppNavigation(authViewModel, userDataViewModel)
            }
        }
    }
}

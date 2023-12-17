package com.amt.instasport.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amt.instasport.auth.AuthenticationManager
import com.amt.instasport.auth.UserDatabaseManager

// GPT code I have no code how this works, I wanted to use Hilt for dependency injections but its not working so Im manually injecting authmanager and userdatabase manager into the viewmodel
class AuthViewModelFactory(
    private val authenticationManager: AuthenticationManager,
    private val userDatabaseManager: UserDatabaseManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authenticationManager, userDatabaseManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

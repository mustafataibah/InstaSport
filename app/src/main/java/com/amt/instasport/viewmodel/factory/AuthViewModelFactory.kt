package com.amt.instasport.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amt.instasport.manager.AuthenticationManager
import com.amt.instasport.viewmodel.AuthViewModel

// GPT code I have no code how this works, I wanted to use Hilt for dependency injections but its not working so Im manually injecting authmanager and userdatabase manager into the viewmodel
class AuthViewModelFactory(
    private val authenticationManager: AuthenticationManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return AuthViewModel(authenticationManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

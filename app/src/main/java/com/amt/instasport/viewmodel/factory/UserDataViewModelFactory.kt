package com.amt.instasport.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amt.instasport.repository.UserRepository
import com.amt.instasport.viewmodel.UserDataViewModel

// Derived from GPT code
class UserDataViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserDataViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

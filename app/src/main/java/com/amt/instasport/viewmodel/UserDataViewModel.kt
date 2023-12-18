package com.amt.instasport.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amt.instasport.model.User
import com.amt.instasport.repository.UserRepository
import kotlinx.coroutines.launch

// Responsible for communicating with the UserRepository to perform operations related to userdata
class UserDataViewModel(private val userRepository: UserRepository) : ViewModel() {
    val currentUserData = MutableLiveData<User?>()

    fun uploadUserData(user: User) {
        viewModelScope.launch {
            userRepository.uploadUserData(user)
            currentUserData.value = user
        }
    }

    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            val user = userRepository.getUser(userId)
            currentUserData.value = user
        }
    }
}

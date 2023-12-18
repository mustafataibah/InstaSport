package com.amt.instasport.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amt.instasport.manager.AuthenticationManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authenticationManager: AuthenticationManager
) : ViewModel() {
    // LiveData to notify the UI about authentication status
    val authenticationState = MutableLiveData<AuthenticationState>()

    fun getCurrentUserId(): String? {
        return authenticationManager.currentUserId
    }

    fun signInWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            val result = authenticationManager.signInWithEmailPassword(email, password)
            authenticationState.value = result
        }
    }

    fun signUpWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            val result = authenticationManager.signUpWithEmailPassword(email, password)
            authenticationState.value = result
        }
    }

    fun signOutFromGoogle(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("839331351515-otct4f6br104ae23ihjm22tlr5hauv8p.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInClient.signOut()
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        viewModelScope.launch {
            val result = authenticationManager.firebaseAuthWithGoogle(idToken)
            authenticationState.value = result
        }
    }

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("839331351515-otct4f6br104ae23ihjm22tlr5hauv8p.apps.googleusercontent.com")
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    fun startPhoneNumberVerification(phoneNumber: String) {
        viewModelScope.launch {
            authenticationManager.startPhoneNumberVerification(phoneNumber)
        }
    }

    val storedVerificationId: String?
        get() = authenticationManager.storedVerificationId

    fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        viewModelScope.launch {
            val result = authenticationManager.verifyPhoneNumberWithCode(verificationId, code)
            authenticationState.value = result
        }
    }

    enum class AuthenticationState {
        AUTHENTICATED, FAILED, NEW_USER, USER_ALREADY_EXISTS, NEW_USER_GOOGLE, INVALID_EMAIL, WEAK_PASSWORD,
    }

    //    private suspend fun fetchUserData() {
//        authenticationManager.currentUserId?.let { userId ->
//            Log.d("AuthViewModel", "Fetching data for user ID: $userId")
//            val user = userDatabaseManager.getUserDataFromDatabase(userId)
//            Log.d("AuthViewModel", "Fetched user data: $user")
//            currentUser.value = user
//        }
//    }
//    fun uploadUserDataToDatabase() {
//        authenticationManager.currentUserId?.let { userId ->
//            val user = User(
//                uid = userId,
//                name = tempUserName.value,
//                age = tempUserAge.value.toIntOrNull() ?: 0,
//                gender = tempUserGender.value
//            )
//            viewModelScope.launch {
//                userDatabaseManager.uploadUserDataToDatabase(user)
//            }
//        } ?: run {
//        }
//    }
}

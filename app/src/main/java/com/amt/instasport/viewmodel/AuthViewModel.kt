package com.amt.instasport.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amt.instasport.manager.AuthenticationManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch


// GPT
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
            .requestEmail().build()
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
            .requestEmail().build()

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

    fun logout() {
        Log.d("AuthViewModel", "log out called")
        viewModelScope.launch {
            val result = authenticationManager.signOut()
            authenticationState.value = result
        }
    }

    enum class AuthenticationState {
        AUTHENTICATED_EMAIL, AUTHENTICATED_GOOGLE, AUTHENTICATED_PHONE,
        NEW_USER, USER_ALREADY_EXISTS, NEW_USER_GOOGLE, INVALID_EMAIL,
        WEAK_PASSWORD, INVALID_USER, INVALID_CREDENTIALS,
        EMAIL_ASSOCIATED_WITH_GOOGLE, FAILED, INVALID_PHONE_CODE,
        INVALID_PHONE_NUMBER, PHONE_NUMBER_ALREADY_EXISTS, NEW_USER_PHONE, SIGNED_OUT
    }
}




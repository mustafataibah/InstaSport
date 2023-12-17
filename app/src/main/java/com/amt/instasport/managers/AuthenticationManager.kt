package com.amt.instasport.managers

import android.util.Log
import com.amt.instasport.data.util.formatToUSPhoneNumber
import com.amt.instasport.ui.viewmodel.AuthViewModel.AuthenticationState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthenticationManager(private val firebaseAuth: FirebaseAuth) {
    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    suspend fun signInWithEmailPassword(email: String, password: String): AuthenticationState {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthenticationState.AUTHENTICATED
        } catch (e: Exception) {
            AuthenticationState.FAILED
        }
    }

    suspend fun signUpWithEmailPassword(email: String, password: String): AuthenticationState {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            AuthenticationState.NEW_USER
        } catch (e: Exception) {
            AuthenticationState.FAILED
        }
    }

    suspend fun firebaseAuthWithGoogle(idToken: String): AuthenticationState {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            firebaseAuth.signInWithCredential(credential).await()
            AuthenticationState.AUTHENTICATED
        } catch (e: Exception) {
            AuthenticationState.FAILED
        }
    }

    var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Handle automatic SMS verification and sign in the user
            Log.d("AuthenticationManager", "onVerificationCompleted:$credential")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Handle verification failure
            Log.w("AuthenticationManager", "onVerificationFailed", e)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d("AuthenticationManager", "onCodeSent:$verificationId")
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    fun startPhoneNumberVerification(phoneNumber: String) {
        val updatedPhoneNumber = formatToUSPhoneNumber(phoneNumber)
        Log.d("AuthenticationManager", "Updated Phone Number: $updatedPhoneNumber")
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(updatedPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun verifyPhoneNumberWithCode(
        verificationId: String?,
        code: String
    ): AuthenticationState {
        if (verificationId.isNullOrEmpty() || code.isBlank()) {
            return AuthenticationState.FAILED
        }
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        return signInWithPhoneAuthCredential(credential)
    }

    private suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): AuthenticationState {
        return try {
            firebaseAuth.signInWithCredential(credential).await()
            AuthenticationState.AUTHENTICATED
        } catch (e: Exception) {
            AuthenticationState.FAILED
        }
    }
}

package com.amt.instasport.manager

import com.amt.instasport.util.formatToUSPhoneNumber
import com.amt.instasport.viewmodel.AuthViewModel.AuthenticationState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
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
            AuthenticationState.AUTHENTICATED_EMAIL
        } catch (e: FirebaseAuthInvalidUserException) {
            AuthenticationState.INVALID_USER
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthenticationState.INVALID_CREDENTIALS
        } catch (e: FirebaseAuthUserCollisionException) {
            AuthenticationState.EMAIL_ASSOCIATED_WITH_GOOGLE
        } catch (e: Exception) {
            AuthenticationState.FAILED
        }
    }

    suspend fun signUpWithEmailPassword(email: String, password: String): AuthenticationState {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            AuthenticationState.NEW_USER
        } catch (e: FirebaseAuthWeakPasswordException) {
            AuthenticationState.WEAK_PASSWORD
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthenticationState.INVALID_EMAIL
        } catch (e: FirebaseAuthUserCollisionException) {
            AuthenticationState.USER_ALREADY_EXISTS
        } catch (e: Exception) {
            AuthenticationState.FAILED
        }
    }

    suspend fun firebaseAuthWithGoogle(idToken: String): AuthenticationState {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

            if (isNewUser) {
                AuthenticationState.NEW_USER_GOOGLE
            } else {
                AuthenticationState.AUTHENTICATED_GOOGLE
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            AuthenticationState.USER_ALREADY_EXISTS
        } catch (e: Exception) {
            AuthenticationState.FAILED
        }
    }

    var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        }

        override fun onVerificationFailed(e: FirebaseException) {
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> AuthenticationState.INVALID_PHONE_NUMBER
                else -> AuthenticationState.FAILED
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    fun startPhoneNumberVerification(phoneNumber: String) {
        val updatedPhoneNumber = formatToUSPhoneNumber(phoneNumber)
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
        return firebaseAuthWithPhone(credential)
    }

    private suspend fun firebaseAuthWithPhone(credential: PhoneAuthCredential): AuthenticationState {
        return try {
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

            if (isNewUser) {
                AuthenticationState.NEW_USER_PHONE
            } else {
                AuthenticationState.AUTHENTICATED_PHONE
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            AuthenticationState.PHONE_NUMBER_ALREADY_EXISTS
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthenticationState.INVALID_PHONE_CODE
        } catch (e: Exception) {
            AuthenticationState.FAILED
        }
    }

    fun signOut(): AuthenticationState {
        firebaseAuth.signOut()
        return AuthenticationState.SIGNED_OUT
    }
}

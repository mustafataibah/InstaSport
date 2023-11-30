package com.amt.instasport

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {
    // [START declare_auth]
    // [START initialize_auth]
    private val auth = Firebase.auth
    // [END initialize_auth]
    // [END declare_auth]

    // LiveData to notify the UI about authentication status
    val authenticationState = MutableLiveData<AuthenticationState>()

    var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    // Initialize phone auth callbacks
    // [START phone_auth_callbacks]
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This will automatically handle the SMS verification and sign in the user
            Log.d("AuthViewModel", "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Handle verification failure
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
//            Log.d(TAG, "onVerificationCompleted:$credential")
//            signInWithPhoneAuthCredential(credential)
            Log.w("AuthViewModel", "onVerificationFailed", e)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d("AuthViewModel", "onCodeSent:$verificationId")
            storedVerificationId = verificationId
            resendToken = token
        }
    }
    // [END phone_auth_callbacks]

    fun startPhoneNumberVerification(phoneNumber: String) = viewModelScope.launch {
        // [START start_phone_auth]
        val updatedPhoneNumber = formatToUSPhoneNumber(phoneNumber)
        Log.d("AuthViewModel", "Updated Phone Number: $updatedPhoneNumber")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(updatedPhoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            // .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    fun verifyPhoneNumberWithCode(verificationId: String?, code: String) = viewModelScope.launch {
        // [START verify_with_code]
        if (verificationId.isNullOrEmpty() || code.isBlank()) {
            // Log error or update UI to show an error message
            return@launch
        }

        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
        Log.d("AuthViewModel", "Verifying")
        // [END verify_with_code]
    }

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) = viewModelScope.launch {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    authenticationState.value = AuthenticationState.AUTHENTICATED
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("AuthViewMode;", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    authenticationState.value = AuthenticationState.FAILED
                    // Update UI
                }
            }
    }
    // [END sign_in_with_phone]

    private fun formatToUSPhoneNumber(input: String): String {
        val digits = input.filter { it.isDigit() }

        return if (digits.startsWith("1")) {
            "+$digits"
        } else {
            "+1$digits"
        }
    }

    enum class AuthenticationState {
        AUTHENTICATED, FAILED
    }

    // REMINDER : Resend verification logic
}

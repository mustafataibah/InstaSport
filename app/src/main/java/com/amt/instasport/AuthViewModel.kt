package com.amt.instasport

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amt.instasport.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {
    var tempUserName = mutableStateOf("")
    var tempUserAge = mutableStateOf("")
    var tempUserGender = mutableStateOf("")

    fun uploadUserDataToDatabase() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userData = User(
                uid = user.uid,
                name = tempUserName.value,
                age = tempUserAge.value.toIntOrNull() ?: 0,
                gender = tempUserGender.value
            )
            val db = Firebase.database.reference
            db.child("users").child(user.uid)
                .setValue(userData)
                .addOnSuccessListener {
                    // Handle success
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
    }


    private val userRepository = UserRepository()
    val userData = MutableLiveData<User?>()
    val userLoadError = MutableLiveData<String>()

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
        AUTHENTICATED, FAILED, USER_NOT_FOUND, NEW_USER, USER_ALREADY_EXISTS
    }

    // REMINDER : Resend verification logic

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("839331351515-otct4f6br104ae23ihjm22tlr5hauv8p.apps.googleusercontent.com")
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                user?.let {
                    userRepository.doesUserExist(it.uid) { exists ->
                        if (exists) {
                            // User exists in the database
                            authenticationState.value = AuthenticationState.USER_ALREADY_EXISTS
                        } else {
                            // User does not exist in the database, navigate to userDetails
                            authenticationState.value = AuthenticationState.NEW_USER
                        }
                    }
                }
            } else {
                // Handle sign-in failure
            }
        }
    }

    private fun checkUserInDatabase(userEmail: String?) {
        // Implement database check logic here
        // Set authenticationState accordingly
    }

    fun addUser(user: User) {
        userRepository.writeUser(user)
    }

    fun getUser(uid: String) {
        userRepository.readUser(uid, onSuccess = { user ->
            // Handle user data
            userData.value = user
        }, onFailure = { exception ->
            // Handle failure
            userLoadError.value = exception?.message ?: "Unknown error occurred"
        })
    }

    fun signUpWithEmailPassword(email: String, password: String) = viewModelScope.launch {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User is created, now check if they exist in database
                    userRepository.doesUserExist(auth.currentUser!!.uid) { exists ->
                        if (exists) {
                            // User exists go to login
                            authenticationState.value = AuthenticationState.USER_ALREADY_EXISTS
                        } else {
                            // New user go to signup
                            authenticationState.value = AuthenticationState.NEW_USER
                        }
                    }
                } else {
                    // Handle failure
                }
            }
    }


    fun signInWithEmailPassword(email: String, password: String) = viewModelScope.launch {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Authentication succeeded
                    authenticationState.value = AuthenticationState.AUTHENTICATED
                } else {
                    // Authentication failed
                    authenticationState.value = AuthenticationState.FAILED
                }
            }
    }
}

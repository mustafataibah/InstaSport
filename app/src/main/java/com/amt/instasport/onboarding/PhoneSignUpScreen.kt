package com.amt.instasport.onboarding

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amt.instasport.R
import com.amt.instasport.auth.AuthenticationManager
import com.amt.instasport.auth.UserDatabaseManager
import com.amt.instasport.ui.viewmodel.AuthViewModel
import com.amt.instasport.ui.viewmodel.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun PhoneSignUpScreen(navController: NavController? = null) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val authenticationManager = AuthenticationManager(firebaseAuth)
    val userDatabaseManager = UserDatabaseManager(firebaseDatabase)
    val factory = AuthViewModelFactory(authenticationManager, userDatabaseManager)
    val viewModel: AuthViewModel = viewModel(factory = factory)
    val authState by viewModel.authenticationState.observeAsState()
    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var isCodeSent by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        if (authState == AuthViewModel.AuthenticationState.AUTHENTICATED) {
            navController?.navigate("userInfo") {
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        VerificationImage(isCodeSent)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Sign Up",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            if (!isCodeSent)
                "Please enter a US phone number, ex. 8571119922"
            else
                "Please enter the one time verification code sent to this number: $phoneNumber",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (!isCodeSent) {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    Log.d(TAG, "Start Phone Number Verification : $phoneNumber")
                    viewModel.startPhoneNumberVerification(phoneNumber)
                    isCodeSent = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Send Verification Code")
            }
            Spacer(modifier = Modifier.height(32.dp))
        } else {
            OutlinedTextField(
                value = verificationCode,
                onValueChange = { verificationCode = it },
                label = { Text("Verification Code") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.verifyPhoneNumberWithCode(
                        viewModel.storedVerificationId,
                        verificationCode
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Verify Code")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun VerificationImage(isCodeSent: Boolean) {
    if (!isCodeSent) {
        Image(
            painter = painterResource(id = R.drawable.phone),
            contentDescription = "Sign Up",
            modifier = Modifier
                .fillMaxWidth()
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.phone2),
            contentDescription = "Sign Up",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPhoneSignUpScreen() {
    PhoneSignUpScreen()
}

private const val TAG = "RegisterScreen"

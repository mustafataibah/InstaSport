package com.amt.instasport

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.livedata.observeAsState


@Composable
fun RegisterScreen( navController: NavController? = null) {
    val viewModel: AuthViewModel = viewModel()
    val authState by viewModel.authenticationState.observeAsState()
    val backIcon = ImageVector.vectorResource(id = R.drawable.outline_chevron_left_24)
    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var isCodeSent by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        if (authState == AuthViewModel.AuthenticationState.AUTHENTICATED) {
            navController?.navigate("dashboard") {
                // This makes sure the back button does not take us back to the registration screen
                popUpTo("loginRegister") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = {
                navController?.navigateUp()
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = backIcon,
                contentDescription = "Back"
            )
        }

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d(TAG, "Start Phone Number Verification : $phoneNumber")
                viewModel.startPhoneNumberVerification(phoneNumber)
                isCodeSent = true
            },
        ) {
            Text("Send Verification Code")
        }
        if (isCodeSent) {
            OutlinedTextField(
                value = verificationCode,
                onValueChange = { verificationCode = it },
                label = { Text("Verification Code") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                viewModel.verifyPhoneNumberWithCode(viewModel.storedVerificationId, verificationCode)
            }) {
                Text("Verify Code")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen()
}


private const val TAG = "RegisterScreen"
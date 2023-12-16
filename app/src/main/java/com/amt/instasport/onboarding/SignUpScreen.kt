package com.amt.instasport.onboarding

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amt.instasport.AuthViewModel
import com.amt.instasport.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun SignUpScreen(navController: NavController? = null) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val isEmailValid = remember(email) { email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) }
    val isPasswordValid =
        remember(password) { password.length >= 8 && password.any { it.isDigit() } && password.any { it.isUpperCase() } }
    val focusManager = LocalFocusManager.current
    val viewModel: AuthViewModel = viewModel()
    val authState by viewModel.authenticationState.observeAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            viewModel.firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Handle exception
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            AuthViewModel.AuthenticationState.NEW_USER -> navController?.navigate("userInfo")
            AuthViewModel.AuthenticationState.USER_ALREADY_EXISTS -> navController?.navigate("login")
            AuthViewModel.AuthenticationState.AUTHENTICATED -> {
                navController?.navigate("dashboard")
            }
            else -> {
                // Handle exceptions
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = { focusManager.clearFocus() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
            .padding(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Create an Account",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.weight(1f))
            OutlinedTextField(value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = image),
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
            )
            Spacer(Modifier.height(65.dp))
            Button(
                onClick = {
                    navController?.navigate("userInfo")
                    if (isEmailValid && isPasswordValid) {
                        viewModel.signUpWithEmailPassword(email, password)
                    } else {
                        // TODO: Show error message
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = isEmailValid && isPasswordValid
            ) {
                Text(
                    "Sign Up", fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    "Or Sign Up with",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Divider(modifier = Modifier.weight(1f))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SocialLoginButton(
                    icon = ImageVector.vectorResource(R.drawable.ic_google), onClick = {
                        val client = viewModel.getGoogleSignInClient(context)
                        val signInIntent = client.signInIntent
                        googleSignInLauncher.launch(signInIntent)
                    }, modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                SocialLoginButton(
                    icon = ImageVector.vectorResource(R.drawable.baseline_smartphone_24),
                    onClick = { /* TODO: Handle Phone Sign Up */ navController?.navigate("phoneSignUp") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.weight(2f))
            TextButton(
                onClick = { navController?.navigate("login") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
                    .width(300.dp)
            ) {
                Text("Already have an account? Login Now")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen()
}

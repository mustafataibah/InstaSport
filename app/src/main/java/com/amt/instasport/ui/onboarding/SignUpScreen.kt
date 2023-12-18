package com.amt.instasport.ui.onboarding

import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amt.instasport.R
import com.amt.instasport.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuthException

@Composable
fun SignUpScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val authState by authViewModel.authenticationState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            authViewModel.firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In failed: ${e.message}", Toast.LENGTH_LONG).show()
        } catch (e: FirebaseAuthException) {
            Toast.makeText(
                context,
                "Firebase authentication failed: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(context, "An unexpected error occurred: ${e.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

//    val googleSignInLauncher = googleSignInLauncher(authViewModel)
//    HandleAuthState(authViewModel, navController)


    LaunchedEffect(authState) {
        when (authState) {
            // Email/Password Auth States
            AuthViewModel.AuthenticationState.NEW_USER -> navController.navigate("userInfo")
            AuthViewModel.AuthenticationState.USER_ALREADY_EXISTS ->
                Toast.makeText(context, "User already exists. Please login.", Toast.LENGTH_SHORT)
                    .show()

            AuthViewModel.AuthenticationState.WEAK_PASSWORD ->
                Toast.makeText(
                    context,
                    "Password is too weak. Please use a stronger password.",
                    Toast.LENGTH_SHORT
                ).show()

            AuthViewModel.AuthenticationState.INVALID_EMAIL ->
                Toast.makeText(
                    context,
                    "Invalid email address. Please check and try again.",
                    Toast.LENGTH_SHORT
                ).show()

            // Google Auth States
            AuthViewModel.AuthenticationState.NEW_USER_GOOGLE -> navController.navigate("userInfo")
            AuthViewModel.AuthenticationState.AUTHENTICATED_GOOGLE -> {
                navController.navigate("dashboard")
                Toast.makeText(
                    context,
                    "This email is associated with a Google account, please login with Google",
                    Toast.LENGTH_SHORT
                ).show()
            }

            AuthViewModel.AuthenticationState.FAILED ->
                Toast.makeText(
                    context,
                    "Something went wrong, please try again!",
                    Toast.LENGTH_SHORT
                ).show()

            else -> {
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
                    authViewModel.signUpWithEmailPassword(
                        email, password
                    )
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
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
                        val client = authViewModel.getGoogleSignInClient(context)
                        val signInIntent = client.signInIntent
                        googleSignInLauncher.launch(signInIntent)
                    }, modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                SocialLoginButton(
                    icon = ImageVector.vectorResource(R.drawable.baseline_smartphone_24),
                    onClick = { navController.navigate("phoneSignUp/SignUp") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.weight(2f))
            TextButton(
                onClick = { navController.navigate("login") },
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
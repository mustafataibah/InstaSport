package com.amt.instasport.ui.onboarding

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.shadow
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
import com.amt.instasport.viewmodel.EventsViewModel
import com.amt.instasport.viewmodel.UserDataViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuthException

@Composable
fun LoginScreen(
    navController: NavController? = null,
    authViewModel: AuthViewModel,
    userDataViewModel: UserDataViewModel,
    eventsViewModel: EventsViewModel
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val authState by authViewModel.authenticationState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        when (authState) {
            AuthViewModel.AuthenticationState.FAILED ->
                Toast.makeText(
                    context,
                    "Something went wrong, please try again",
                    Toast.LENGTH_SHORT
                ).show()

            // Email Auth States
            AuthViewModel.AuthenticationState.AUTHENTICATED_EMAIL -> {
                authViewModel.getCurrentUserId()?.let { userId ->
                    userDataViewModel.fetchUserData(userId)
                }
                eventsViewModel.fetchAllEvents()
                navController?.navigate("dashboard")
            }

            AuthViewModel.AuthenticationState.INVALID_USER -> {
                Toast.makeText(context, "No user Associated with this email", Toast.LENGTH_SHORT)
                    .show()
            }

            AuthViewModel.AuthenticationState.INVALID_CREDENTIALS -> {
                Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show()
            }

            AuthViewModel.AuthenticationState.EMAIL_ASSOCIATED_WITH_GOOGLE -> {
                Toast.makeText(
                    context,
                    "This email is associated with a google account, please sign in with google",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Google Auth States
            AuthViewModel.AuthenticationState.AUTHENTICATED_GOOGLE -> {
                authViewModel.getCurrentUserId()?.let { userId ->
                    userDataViewModel.fetchUserData(userId)
                }
                eventsViewModel.fetchAllEvents()
                navController?.navigate("dashboard")
            }

            AuthViewModel.AuthenticationState.NEW_USER_GOOGLE -> {
                Toast.makeText(
                    context,
                    "This email is not associated with any account, please sign up!",
                    Toast.LENGTH_SHORT
                ).show()
                navController?.navigate("userInfo")
            }

            else -> {
            }
        }
    }

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
                "Ready to play?",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.weight(1f))
            OutlinedTextField(value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
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
                singleLine = true
            )
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { /* TODO: Handle forgot password */ },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("Forgot Password?", modifier = Modifier.padding(end = 8.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { authViewModel.signInWithEmailPassword(email, password) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    "Login", fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    "Or Login with",
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
                    icon = ImageVector.vectorResource(R.drawable.ic_google),
                    onClick = {
                        val client = authViewModel.getGoogleSignInClient(context)
                        val signInIntent = client.signInIntent
                        authViewModel.signOutFromGoogle(context)
                        googleSignInLauncher.launch(signInIntent)
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                SocialLoginButton(
                    icon = ImageVector.vectorResource(R.drawable.baseline_smartphone_24),
                    onClick = { navController?.navigate("phoneSignUp/Login") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.weight(2f))
            TextButton(
                onClick = { navController?.navigate("signUp") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            ) {
                Text("Don't have an account? Sign Up Now")
            }
        }
    }
}

@Composable
fun SocialLoginButton(icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Login with social",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(40.dp)
                .width(300.dp)
                .padding(10.dp)
        )

    }
}

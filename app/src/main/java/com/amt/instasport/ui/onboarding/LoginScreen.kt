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
import com.amt.instasport.viewmodel.UserDataViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    navController: NavController? = null,
    authViewModel: AuthViewModel,
    userDataViewModel: UserDataViewModel
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val authState by authViewModel.authenticationState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        when (authState) {
            // 3 Cases when a user logs in
            // 1- If logs in with google and email already exists AUTHENTICATED
            // 2- Logs in with google and its a new account NEW_USER_GOOGLE
            // 3- Wrong Email/Password FAILED
            // 4-
            AuthViewModel.AuthenticationState.AUTHENTICATED -> {
                authViewModel.getCurrentUserId()?.let { userId ->
                    userDataViewModel.fetchUserData(userId)
                }
                navController?.navigate("dashboard")
            }

            AuthViewModel.AuthenticationState.NEW_USER_GOOGLE -> {
                navController?.navigate("userInfo")
            }

            AuthViewModel.AuthenticationState.FAILED -> {
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_LONG).show()
            }
            // TODO: Handle invalid email, wrong password, account already exists, and other cases
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
            // TODO: Handle exceptions
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
                    // TODO: Create a new phoneLogin screen similar to phoneSignUp but different title and nav
                    onClick = { navController?.navigate("phoneSignUp") },
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

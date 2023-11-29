package com.example.instasport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlin.math.log


class LoginRegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginRegisterNavigation()
        }
    }
}


@Composable
fun LoginRegisterNavigation() {
    val navController = rememberNavController()

    // Remove NavHost from here and add it to main activity and let main activity act like a container for all the app because right now
    // I am assuming that you need to define all pages here to navigate using the same navController defined here because if you remove dashboard
    // even though you are not navigating to it from this page but from login page the app crashes
    NavHost(navController = navController, startDestination = "login_register") {
        composable("login_register") { LoginRegisterScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
    }
}

@Composable
fun LoginRegisterScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val logoPainter = painterResource(id = R.drawable.logo)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Image(
                painter = logoPainter,
                contentDescription = "Logo",
                modifier = Modifier.fillMaxSize()
            )
        }

        // Buttons and clickable text
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { navController.navigate("register") }) {
                Text("Sign Up with Email")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {}) {
                Text("Sign Up with Google")
            }

            Spacer(modifier = Modifier.height(16.dp))

            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White)) {
                    append("Already a user? ")
                }
                pushStringAnnotation(tag = "LOG_IN", annotation = "login")
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, color = Color.White)) {
                    append("Log In")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "LOG_IN", start = offset, end = offset)
                        .firstOrNull()?.let { navController.navigate("login") }
                }
            )
        }
    }
}
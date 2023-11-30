package com.amt.instasport

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.background


@Composable
fun LoginRegisterScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val logoPainter = painterResource(id = R.drawable.instasport_logo)
        val typoPainter = painterResource(id = R.drawable.typography)

        Spacer(modifier = Modifier.weight(2f))

        Image(
            painter = logoPainter,
            contentDescription = "Logo",
            modifier = Modifier.weight(1f)
        )

        Image(
            painter = typoPainter,
            contentDescription = "Typography",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .weight(0.6f)
        )

        // Buttons and clickable text
        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navController?.navigate("register")
                },
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
            ) {
                Text("Sign Up with Phone")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {},
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
            ) {
                Text("Sign Up with Google")
            }

            Spacer(modifier = Modifier.height(20.dp))

            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append("Already a user? ")
                }
                pushStringAnnotation(tag = "LOG_IN", annotation = "login")
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, color = Color.Gray)) {
                    append("Log In")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "LOG_IN", start = offset, end = offset)
                        .firstOrNull()?.let { navController?.navigate("login") }
                }
            )
        }

        Spacer(modifier = Modifier.weight(2f))

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginRegisterScreen() {
    LoginRegisterScreen()
}


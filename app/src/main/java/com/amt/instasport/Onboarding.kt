package com.amt.instasport

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun OnboardingScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome")

        val image: Painter = painterResource(id = R.drawable.logo)
        Image(
            painter = image,
            contentDescription = "Onboarding Image",
            modifier = Modifier.fillMaxWidth().weight(1f)
        )

        Button(
            onClick = {} ,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Continue")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingScreen() {
    OnboardingScreen()
}

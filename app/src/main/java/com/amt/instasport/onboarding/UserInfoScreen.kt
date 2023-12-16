package com.amt.instasport.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amt.instasport.AuthViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun UserInfoScreen(navController: NavHostController? = null) {
    val viewModel: AuthViewModel = viewModel()
    val pagerState = rememberPagerState(
        pageCount = 4
    )
    Column(
        Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState, Modifier.weight(1f)
        ) { page ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (page) {
                    0 -> AgeInput(viewModel)
                    1 -> NameInput(viewModel)
                    2 -> GenderInput(viewModel)
                    3 -> Spacer(modifier = Modifier.fillMaxSize())
                }
            }
        }

        PageIndicator(
            pageCount = 4, currentPage = pagerState.currentPage, modifier = Modifier.padding(60.dp)
        )

        if (pagerState.currentPage == 3) {
            LaunchedEffect(Unit) {
                // Why is this not popping userInfo from backstack??? because after a user goes to dashboard and swipes left they can go back to this page
                viewModel.uploadUserDataToDatabase()
                navController?.navigate("dashboard")
            }
        }
    }
}

@Composable
fun AgeInput(viewModel: AuthViewModel) {
    var age by remember { mutableStateOf("") }
    Text(
        text = "How old are you?", fontSize = 18.sp, fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = age,
        onValueChange = {
            age = it
            viewModel.tempUserAge.value = age
        },
        label = { Text("Age") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}


@Composable
fun NameInput(viewModel: AuthViewModel) {
    var name by remember { mutableStateOf("") }
    Text(
        text = "What should we call you?", fontSize = 18.sp, fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(value = name, onValueChange = {
        name = it
        viewModel.tempUserName.value = name
    }, label = { Text("Name") }, singleLine = true
    )
}


@Composable
fun GenderInput(viewModel: AuthViewModel) {
    val genderOptions = listOf("Male", "Female", "Other")
    var selectedGender by remember { mutableStateOf(genderOptions[0]) }

    Text(
        text = "Select Your Gender", fontSize = 18.sp, fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(8.dp))
    genderOptions.forEach { gender ->
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = (gender == selectedGender), onClick = {
                selectedGender = gender
                viewModel.tempUserGender.value = gender
            })
            Text(text = gender, modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserInfoScreen() {
    UserInfoScreen()
}

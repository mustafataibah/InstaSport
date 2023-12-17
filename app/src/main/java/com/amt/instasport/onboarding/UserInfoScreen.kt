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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.amt.instasport.data.model.User
import com.amt.instasport.ui.viewmodel.AuthViewModel
import com.amt.instasport.ui.viewmodel.UserDataViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun UserInfoScreen(
    navController: NavHostController? = null,
    authViewModel: AuthViewModel,
    userDataViewModel: UserDataViewModel
) {
    var ageText by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }

    val pagerState = rememberPagerState(pageCount = 4)

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
                    0 -> AgeInput(ageText, onAgeChange = { ageText = it })
                    1 -> NameInput(name, onNameChange = { name = it })
                    2 -> GenderInput(gender, onGenderChange = { gender = it })
                    3 -> Spacer(modifier = Modifier.fillMaxSize())
                }
            }
        }

        PageIndicator(
            pageCount = 4, currentPage = pagerState.currentPage, modifier = Modifier.padding(60.dp)
        )

        if (pagerState.currentPage == 3) {
            LaunchedEffect(Unit) {
                val uid = authViewModel.getCurrentUserId()
                val age = ageText.toIntOrNull() ?: 0
                if (uid != null) {
                    val user = User(uid = uid, name = name, age = age, gender = gender)
                    // TODO: User needs to login for the data to be fetched, make it such that signing up also makes the data accessible or something
                    userDataViewModel.uploadUserData(user)
                    navController?.navigate("dashboard")
                }
            }
        }
    }
}

@Composable
fun AgeInput(ageText: String, onAgeChange: (String) -> Unit) {
    Text(
        text = "How old are you?", fontSize = 18.sp, fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = ageText,
        onValueChange = onAgeChange,
        label = { Text("Age") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}


@Composable
fun NameInput(name: String, onNameChange: (String) -> Unit) {
    Text(
        text = "What should we call you?", fontSize = 18.sp, fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = name, onValueChange = onNameChange, label = { Text("Name") }, singleLine = true
    )
}


@Composable
fun GenderInput(selectedGender: String, onGenderChange: (String) -> Unit) {
    val genderOptions = listOf("Male", "Female", "Other")

    Text(
        text = "Select Your Gender", fontSize = 18.sp, fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(8.dp))
    genderOptions.forEach { gender ->
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = (gender == selectedGender), onClick = { onGenderChange(gender) })
            Text(text = gender, modifier = Modifier.padding(start = 8.dp))
        }
    }
}
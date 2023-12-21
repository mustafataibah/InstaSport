package com.amt.instasport.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.amt.instasport.R
import com.amt.instasport.model.User
import com.amt.instasport.ui.component.PageIndicator
import com.amt.instasport.viewmodel.AuthViewModel
import com.amt.instasport.viewmodel.UserDataViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun UserInfoScreen(
    navController: NavHostController? = null,
    authViewModel: AuthViewModel? = null,
    userDataViewModel: UserDataViewModel? = null
) {
    var ageText by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    val selectedSports = remember { mutableStateListOf<String>() }

    val focusManager = LocalFocusManager.current
    val pagerState = rememberPagerState(pageCount = 5)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.backgroundimage),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }, horizontalAlignment = Alignment.CenterHorizontally
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
                        3 -> SportsInterestInput(selectedSports = selectedSports)
                        4 -> Spacer(modifier = Modifier.fillMaxSize())
                    }
                }
            }

            PageIndicator(
                pageCount = 5,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(60.dp)
            )

            if (pagerState.currentPage == 4) {
                LaunchedEffect(Unit) {
                    val uid = authViewModel?.getCurrentUserId()
                    val age = ageText.toIntOrNull() ?: 0

                    if (uid != null) {
                        val user = User(
                            uid = uid,
                            name = name,
                            age = age,
                            gender = gender,
                            followedSports = selectedSports.toList()
                        )
                        // TODO: User needs to login for the data to be fetched, make it such that signing up also makes the data accessible or something
                        userDataViewModel?.uploadUserData(user)
                        navController?.navigate("location")
                    }
                }
            }
        }
    }
}

@Composable
fun AgeInput(ageText: String, onAgeChange: (String) -> Unit) {
    Text(
        text = "How old are you?", fontSize = 36.sp, fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(20.dp))
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
        text = "What's your name?", fontSize = 36.sp, fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(20.dp))
    OutlinedTextField(
        value = name, onValueChange = onNameChange, label = { Text("Name") }, singleLine = true
    )
}


@Composable
fun GenderInput(selectedGender: String, onGenderChange: (String) -> Unit) {
    val genderOptions = listOf("Male", "Female", "Other")

    Text(
        text = "Select Your Gender", fontSize = 36.sp, fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(20.dp))
    genderOptions.forEach { gender ->
        OutlinedButton(
            onClick = { onGenderChange(gender) }, colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (gender == selectedGender) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                contentColor = if (gender == selectedGender) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            ), modifier = Modifier
                .width(150.dp)
                .padding(vertical = 4.dp)
        ) {
            Text(text = gender, modifier = Modifier.padding(8.dp), fontSize = 16.sp)
        }
    }
}

@Composable
fun SportsInterestInput(selectedSports: MutableList<String>) {
    val sports = listOf("football", "volleyball", "basketball", "badminton", "tennis", "squash")
    Spacer(Modifier.height(100.dp))

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sports.chunked(2)) { rowSports ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowSports.forEach { sport ->
                    SportImage(
                        sportName = sport,
                        isSelected = selectedSports.contains(sport),
                        onClick = {
                            if (selectedSports.contains(sport)) {
                                selectedSports.remove(sport)
                            } else if (selectedSports.size < 3) {
                                selectedSports.add(sport)
                            }
                        })
                }
            }
        }
    }
    Spacer(Modifier.height(30.dp))
    Text(
        text = "${selectedSports.size}/3 selected",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    )
}


@Composable
fun SportImage(sportName: String, isSelected: Boolean, onClick: () -> Unit) {
    val imageResId = when (sportName) {
        "football" -> R.drawable.football
        "volleyball" -> R.drawable.volleyball
        "basketball" -> R.drawable.basketball
        "badminton" -> R.drawable.badminton
        "tennis" -> R.drawable.tennis
        "squash" -> R.drawable.squash
        else -> R.drawable.instasport_logo
    }

    val image: Painter = painterResource(id = imageResId)
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val overlayColor = if (isSelected) Color.Black.copy(alpha = 0.6f) else Color.Transparent

    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(BorderStroke(if (isSelected) 4.dp else 0.dp, borderColor))
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = image,
            contentDescription = sportName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(overlayColor)
            )
        }
    }
}

@Preview
@Composable
fun PreviewUserInfoScreen() {
    UserInfoScreen()
}





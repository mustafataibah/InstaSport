package com.amt.instasport.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amt.instasport.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsOnboardingScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("How Old Are You?") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        UserDetailsOnboardingScreenContent(navController, innerPadding)
    }
}

@Composable
fun UserDetailsOnboardingScreenContent(navController: NavController, innerPadding: PaddingValues) {
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Select Gender") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.weight(1f))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(8f),
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = { isDropdownExpanded = true }) {
            Text(gender)
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.outline_chevron_left_24), contentDescription = null)
        }

        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            DropdownMenuItem(text = {"Male"}, onClick = {
                gender = "Male"
                isDropdownExpanded = false
            })
            DropdownMenuItem(text = {"Female"}, onClick = {
                gender = "Female"
                isDropdownExpanded = false
            })
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("sportsInterestsOnboarding") },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.9f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Continue", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}





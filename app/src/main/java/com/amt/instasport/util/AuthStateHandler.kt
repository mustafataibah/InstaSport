package com.amt.instasport.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.amt.instasport.viewmodel.AuthViewModel

@Composable
fun HandleAuthState(
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val authState by authViewModel.authenticationState.observeAsState()

    LaunchedEffect(authState) {
        when (authState) {
            AuthViewModel.AuthenticationState.NEW_USER -> navController.navigate("userInfo")
            AuthViewModel.AuthenticationState.NEW_USER_GOOGLE -> navController.navigate("userInfo")
            AuthViewModel.AuthenticationState.AUTHENTICATED -> navController.navigate("dashboard")
            // Add other cases
            else -> {}
        }
    }
}
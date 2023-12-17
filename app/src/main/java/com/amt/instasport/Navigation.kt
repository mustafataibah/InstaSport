package com.amt.instasport

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amt.instasport.onboarding.LandingScreen
import com.amt.instasport.onboarding.LoginScreen
import com.amt.instasport.onboarding.OnboardingScreen
import com.amt.instasport.onboarding.PhoneSignUpScreen
import com.amt.instasport.onboarding.SignUpScreen
import com.amt.instasport.onboarding.UserInfoScreen
import com.amt.instasport.ui.viewmodel.AuthViewModel
import com.amt.instasport.ui.viewmodel.UserDataViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    userDataViewModel: UserDataViewModel,
//    eventsViewModel: EventsViewModel
) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    Scaffold(topBar = {
        if (currentDestination?.route in listOf("dashboard", "host", "events", "profile")) {
            currentDestination?.route?.let { TopBar(title = it, navController) }
        }
    }, bottomBar = {
        if (currentDestination?.route in listOf("dashboard", "host", "events", "profile")) {
            BottomNavBar(navController)
        }
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "onboarding",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Onboarding Screens
            composable("onboarding") { OnboardingScreen(navController) }
            composable("landing") { LandingScreen(navController) }
            composable("login") { LoginScreen(navController, authViewModel, userDataViewModel) }
            composable("signUp") { SignUpScreen(navController, authViewModel) }
            composable("phoneSignUp") { PhoneSignUpScreen(navController, authViewModel) }
            composable("userInfo") {
                UserInfoScreen(
                    navController,
                    authViewModel,
                    userDataViewModel
                )
            }

            // Main App Screens
            composable("dashboard") { DashboardScreen(navController) }
            composable("host") { HostScreen(navController) }
            composable("events") { EventsScreen(navController) }
            composable("profile") { ProfileScreen(navController, userDataViewModel) }
        }
    }
}


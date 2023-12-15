package com.amt.instasport

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amt.instasport.onboarding.LandingScreen
import com.amt.instasport.onboarding.LoginScreen
import com.amt.instasport.onboarding.OnboardingScreen
import com.amt.instasport.onboarding.SignUpScreen
import com.amt.instasport.onboarding.UserInfoScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
//    val isOnboardingFinished by remember {
//        mutableStateOf(sharedPreferences.getBoolean("isFinished", false))
//    }
//    val startDestination = if (isOnboardingFinished) "landing" else "onboarding"

    Scaffold(
        topBar = {
            if (currentDestination?.route in listOf("dashboard", "host", "events", "profile")) {
                currentDestination?.route?.let { TopBar(title = it, navController) }
            }
        },
        bottomBar = {
            if (currentDestination?.route in listOf("dashboard", "host", "events", "profile")) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "onboarding", modifier = Modifier.padding(innerPadding)) {
            // Onboarding Screens
            composable("onboarding") { OnboardingScreen(navController, context as MainActivity) }
            composable("landing") { LandingScreen(navController) }
            composable("login") { LoginScreen(navController) }
            composable("signUp") { SignUpScreen(navController) }
            composable("userInfo") { UserInfoScreen(navController) }

            // Dashboard
            composable("dashboard") { DashboardScreen(navController) }
            composable("host") { HostScreen(navController) }
            composable("events") { EventsScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
        }
    }
}


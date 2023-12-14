package com.amt.instasport

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amt.instasport.onboarding.LandingScreen
import com.amt.instasport.onboarding.LoginScreen
import com.amt.instasport.onboarding.OnboardingScreen
import com.amt.instasport.onboarding.SignUpScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    val isOnboardingFinished by remember {
        mutableStateOf(sharedPreferences.getBoolean("isFinished", false))
    }
    val startDestination = if (isOnboardingFinished) "landing" else "onboarding"


    NavHost(navController = navController, startDestination = startDestination) {
        // Onboarding Screens
        composable("onboarding") { OnboardingScreen(navController, context as MainActivity) }
//        composable("usernameOnboarding") { UsernameOnboardingScreen(navController) }
//        composable("userDetailsOnboarding") { UserDetailsOnboardingScreen(navController) }
//        composable("genderOnboarding") { GenderOnboardingScreen(navController) }
//        composable("sportsInterestsOnboarding") { SportsInterestOnboardingScreen(navController) }
        composable("landing") { LandingScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signUp") { SignUpScreen(navController)}

        // Dashboard
        composable("dashboard") { DashboardScreen(navController) }
    }
}


package com.amt.instasport

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amt.instasport.onboarding.CommunityOnboardingScreen
import com.amt.instasport.onboarding.EventsOnboardingScreen
import com.amt.instasport.onboarding.LoginRegisterScreen
import com.amt.instasport.onboarding.LoginScreen
import com.amt.instasport.onboarding.RegisterScreen
import com.amt.instasport.onboarding.SportsInterestOnboardingScreen
import com.amt.instasport.onboarding.UserDetailsOnboardingScreen
import com.amt.instasport.onboarding.UsernameOnboardingScreen
import com.amt.instasport.onboarding.WelcomeOnboardingScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcomeOnboarding") {
        // Onboarding Screens
        composable("welcomeOnboarding") { WelcomeOnboardingScreen(navController) }
        composable("communityOnboarding") { CommunityOnboardingScreen(navController) }
        composable("eventsOnboarding") { EventsOnboardingScreen(navController) }
        composable("usernameOnboarding") { UsernameOnboardingScreen(navController) }
        composable("userDetailsOnboarding") { UserDetailsOnboardingScreen(navController) }
        composable("sportsInterestsOnboarding") {  SportsInterestOnboardingScreen(navController) }
        composable("loginRegister") { LoginRegisterScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }

        // Dashboard
        composable("dashboard") { DashboardScreen(navController) }
    }
}


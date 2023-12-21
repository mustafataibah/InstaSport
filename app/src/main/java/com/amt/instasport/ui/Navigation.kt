// navigation
package com.amt.instasport.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amt.instasport.ui.component.BottomNavBar
import com.amt.instasport.ui.component.TopBar
import com.amt.instasport.ui.onboarding.LandingScreen
import com.amt.instasport.ui.onboarding.LoginScreen
import com.amt.instasport.ui.onboarding.OnboardingScreen
import com.amt.instasport.ui.onboarding.PhoneAuthScreen
import com.amt.instasport.ui.onboarding.SignUpScreen
import com.amt.instasport.ui.onboarding.UserInfoScreen
import com.amt.instasport.ui.view.DashboardScreen
import com.amt.instasport.ui.view.EventsScreen
import com.amt.instasport.ui.view.GoogleMapComposable
import com.amt.instasport.ui.view.HostScreen
import com.amt.instasport.ui.view.ProfileScreen
import com.amt.instasport.ui.view.SettingsScreen
import com.amt.instasport.viewmodel.AuthViewModel
import com.amt.instasport.viewmodel.EventsViewModel
import com.amt.instasport.viewmodel.UserDataViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    userDataViewModel: UserDataViewModel,
    eventsViewModel: EventsViewModel
) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    Scaffold(topBar = {
        if (currentDestination?.route in listOf(
                "dashboard", "host", "events", "profile", "settings", "location"
            )
        ) {
            currentDestination?.route?.let { TopBar(title = it, navController) }
        }
    }, bottomBar = {
        if (currentDestination?.route in listOf(
                "dashboard", "host", "events", "profile", "settings"
            )
        ) {
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
            composable("phoneSignUp/{signUpOrLogin}") { backStackEntry ->
                val signUpOrLogin = backStackEntry.arguments?.getString("signUpOrLogin") ?: "SignUp"
                PhoneAuthScreen(navController, authViewModel, signUpOrLogin)
            }
            composable("userInfo") {
                UserInfoScreen(
                    navController, authViewModel, userDataViewModel
                )
            }

            // Main App Screens
            composable("dashboard") { DashboardScreen(navController,userDataViewModel,) }
            composable("host") { HostScreen(navController,authViewModel,eventsViewModel,userDataViewModel) }
            composable("events") { EventsScreen() }
            composable("profile") { ProfileScreen(userDataViewModel) }
            composable("settings") { SettingsScreen(navController, authViewModel) }
            composable("location") { GoogleMapComposable(navController) }

        }
    }
}
package com.amt.instasport

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            if (currentDestination?.route in listOf("dashboard", "events", "profile")) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "loginRegister", modifier = Modifier.padding(innerPadding)) {
            composable("loginRegister") { LoginRegisterScreen(navController) }
            composable("register") { RegisterScreen(navController) }
            composable("login") { LoginScreen(navController) }
            composable("dashboard") { DashboardScreen(navController) }
            composable("events") { EventsScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
        }
    }
}
package com.amt.instasport

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icons.Outlined.Home },
            label = { "Dashboard" },
            selected = false,
            onClick = { navController.navigate("dashboard") }
        )
        NavigationBarItem(
            icon = { Icons.Outlined.List },
            label = { "Events" },
            selected = false,
            onClick = { navController.navigate("events") }
        )
        NavigationBarItem(
            icon = { Icons.Outlined.Person },
            label = { "Profile" },
            selected = false,
            onClick = { navController.navigate("profile") }
        )
    }
}


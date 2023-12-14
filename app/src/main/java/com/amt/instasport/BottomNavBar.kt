package com.amt.instasport

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar () {
        NavigationBarItem(
            icon = {
                Icon (imageVector = if (currentRoute == "dashboard") Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Dashboard")
            },
            label = { Text ("Dashboard") },
            selected = currentRoute == "dashboard",
            onClick = {
                if (currentRoute != "dashboard") {
                    navController.navigate("dashboard")
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon (imageVector = if (currentRoute == "host") Icons.Filled.Add else Icons.Outlined.Add,
                    contentDescription = "Host")
            },
            label = { Text ("Host") },
            selected = currentRoute == "host",
            onClick = {
                if (currentRoute != "host") {
                    navController.navigate("host")
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon (imageVector = if (currentRoute == "events") Icons.Filled.List else Icons.Outlined.List,
                    contentDescription = "Events")
            },
            label = { Text ("Events") },
            selected = currentRoute == "events",
            onClick = {
                if (currentRoute != "events") {
                    navController.navigate("events")
                }
            }
        )
    }
}


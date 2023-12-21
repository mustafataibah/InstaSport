package com.amt.instasport.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amt.instasport.ui.theme.InstaSportFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, navController: NavController) {
    val capitalizedTitle = title.replaceFirstChar { it.titlecase() }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    CenterAlignedTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = Color.White,
    ), title = {
        Text(
            text = capitalizedTitle,
            fontFamily = InstaSportFont,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
    }, navigationIcon = {
        val navIcon = when (currentRoute) {
            "host" -> Icons.Default.ArrowBack
            else -> null
        }
        IconButton(onClick = { navController.navigateUp() }) {
            if (navIcon != null) {
                Icon(
                    imageVector = navIcon,
                    contentDescription = "Back",
                    tint = Color.White,
                )
            }
        }
    }, actions = {
        val settingsIcon = when (currentRoute) {
            "profile" -> Icons.Outlined.Settings
            "settings" -> Icons.Filled.Settings
            else -> null
        }
        IconButton(onClick = { navController.navigate("settings") }) {
            if (settingsIcon != null) {
                Icon(
                    imageVector = settingsIcon,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
        }
        if (currentRoute != "location") {
            val profileIcon =
                if (currentRoute == "profile") Icons.Filled.Person else Icons.Outlined.Person
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(
                    imageVector = profileIcon,
                    contentDescription = "Profile",
                    tint = Color.White
                )
            }
        }
    })
}

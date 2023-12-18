package com.amt.instasport

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SettingsScreen (navController: NavController? = null) {
    Column (
        modifier =
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // Account Settings
        Text (text = "Account Settings",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 8.dp),
            fontSize = 20.sp,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Card (modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 4.dp
                    )
            ) {
                item {
                    SettingsItem(title = "Edit Profile", switchable = false)
                    Divider()
                }
                item {
                    SettingsItem(title = "Change Password", switchable = false)
                    Divider()
                }
                item {
                    SettingsItem(title = "Privacy", switchable = false)
                    Divider()
                }
                item {
                    SettingsItem(title = "Notifications", switchable = true)
                }
            }
        }

        Divider (modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))

        // Actions Settings
        Text (text = "Actions",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 8.dp),
            fontSize = 20.sp,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Card (modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 4.dp
                    )
            ) {
                item {
                    SettingsItem(title = "Add Account", switchable = false)
                    Divider()
                }
                item {
                    SettingsItem(title = "Payment Methods", switchable = false)
                    Divider()
                }
                item {
                    SettingsItem(title = "Log Out", switchable = false, Icons.Default.AccountBox)
                }
            }
        }
    }
}

@Composable
private fun SettingsItem (
    title: String,
    switchable: Boolean,
    icon: ImageVector? = Icons.Default.KeyboardArrowRight
) {
    Row(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ClickableText(
            text = AnnotatedString(title),
            onClick = { /*TODO*/ }
        )
        if (switchable) {
            Switch()
        } else {
            icon?.let {
                Icon(it, contentDescription = null)
            }
        }
    }
}

@Composable
fun Switch() {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = { checked = it },
        modifier = Modifier
            .scale(0.75f)
            .size(width = 40.dp, height = 24.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen()
}

package com.amt.instasport.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amt.instasport.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(navController: NavController? = null, authViewModel: AuthViewModel? = null) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // Account Settings
        Text(
            text = "Account Settings",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 8.dp),
            fontSize = 20.sp,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Card(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp
                    )
            ) {
                item {
                    SettingsItem(title = "Edit Profile", switchable = false, onClick = {})
                    Divider()
                }
                item {
                    SettingsItem(title = "Change Password", switchable = false, onClick = {})
                    Divider()
                }
                item {
                    SettingsItem(title = "Privacy", switchable = false, onClick = {})
                    Divider()
                }
                item {
                    SettingsItem(title = "Notifications", switchable = true, onClick = {})
                }
            }
        }

        Divider(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))

        // Actions Settings
        Text(
            text = "Actions",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 8.dp),
            fontSize = 20.sp,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Card(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp
                    )
            ) {
                item {
                    SettingsItem(title = "Add Account", switchable = false, onClick = {})
                    Divider()
                }
                item {
                    SettingsItem(title = "Payment Methods", switchable = false, onClick = {})
                    Divider()
                }
                item {
                    SettingsItem(title = "Log Out",
                        switchable = false,
                        Icons.Default.AccountBox,
                        onClick = { showLogoutDialog = true })
                }
            }
            if (showLogoutDialog) {
                LogoutDialog(onConfirm = {
                    showLogoutDialog = false
                    authViewModel?.logout()
                    navController?.navigate("landing")
                }, onDismiss = {
                    showLogoutDialog = false
                })
            }
        }
    }
}

@Composable
private fun LogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text(text = "Log Out", fontWeight = FontWeight.Bold) },
        text = { Text("Are you sure you want to log out?", fontSize = 18.sp) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Log Out", fontSize = 18.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontSize = 18.sp)
            }
        })
}

@Composable
private fun SettingsItem(
    title: String,
    switchable: Boolean,
    icon: ImageVector? = Icons.Default.KeyboardArrowRight,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 12.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = AnnotatedString(title))
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

//@Preview(showBackground = true)
//@Composable
//fun PreviewSettingsScreen() {
//    SettingsScreen()
//}

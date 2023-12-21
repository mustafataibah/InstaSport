package com.amt.instasport.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amt.instasport.R
import com.amt.instasport.ui.theme.InstaSportFont
import com.amt.instasport.util.sportIconMap
import com.amt.instasport.viewmodel.UserDataViewModel

@Composable
fun ProfileScreen(userDataViewModel: UserDataViewModel) {
    val currentUser by userDataViewModel.currentUserData.observeAsState()
    val (isEditing, setIsEditing) = remember { mutableStateOf(false) }
    val (editedName, setEditedName) = remember { mutableStateOf("") }
    val (followedSports, setFollowedSports) = remember {
        mutableStateOf(currentUser?.followedSports.orEmpty().map {
            it.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase() else char.toString() }
        })
    }

    // Update function
    val updateSports: (Int, String) -> Unit = { index, newSport ->
        val updatedSports = followedSports.toMutableList().apply {
            set(index, newSport)
        }
        setFollowedSports(updatedSports)
        currentUser?.followedSports = updatedSports
        userDataViewModel.uploadUserData(currentUser!!)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Column(
            modifier = Modifier.padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.instasport_logo),
                    contentDescription = "User Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            currentUser?.let { user ->
                if (isEditing) {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { setEditedName(it) },
                        label = { Text("Edit Name") },
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        OutlinedButton(onClick = {
                            setIsEditing(false)
                        }) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            setIsEditing(false)
                            user.name = editedName
                            userDataViewModel.uploadUserData(user)
                        }) {
                            Text("Save")
                        }
                    }

                } else {
                    Text(
                        text = user.name,
                        fontFamily = InstaSportFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                    )
                    Button(
                        onClick = {
                            setIsEditing(true)
                            setEditedName(user.name)
                        }, modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Edit Name")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Icon(
                    Icons.Filled.Place,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text(
                    text = "Boston, MA",
                    modifier = Modifier.padding(end = 8.dp),
                    fontFamily = InstaSportFont,
                    fontWeight = FontWeight.Normal,
                )
            }
        }

        Spacer(modifier = Modifier.padding(12.dp))

        Divider()

        Spacer(modifier = Modifier.height(4.dp))

        // Skill Blocks
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "My Sports",
                fontFamily = InstaSportFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
            )
            followedSports.forEachIndexed { index, sport ->
                SportsBlock(
                    sport = sport,
                    followedSports = followedSports,
                    onSportChanged = { newSport -> updateSports(index, newSport) }
                )
            }
        }
    }
}

@Composable
fun SportsBlock(sport: String, followedSports: List<String>, onSportChanged: (String) -> Unit) {
    val (isDialogOpen, setIsDialogOpen) = remember { mutableStateOf(false) }
    val (currentSport, setCurrentSport) = remember { mutableStateOf(sport) }
    val sportsOptions = listOf("football", "volleyball", "basketball", "badminton", "tennis", "squash")
    val iconResId = sportIconMap[currentSport.lowercase()] ?: R.drawable.sportsicon

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "Sport Icon",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = currentSport.replaceFirstChar { it.uppercaseChar() },
                fontSize = 18.sp,
                fontFamily = InstaSportFont,
                fontWeight = FontWeight.Normal,
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.Black
                ),
                onClick = { setIsDialogOpen(true) }
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(20.dp))
            }
        }

        if (isDialogOpen) {
            AlertDialog(
                onDismissRequest = { setIsDialogOpen(false) },
                title = { Text(text = "Select a Sport") },
                text = {
                    Column {
                        sportsOptions.forEach { sportOption ->
                            val isDisabled = sportOption.replaceFirstChar { it.uppercaseChar() } in followedSports && sportOption.lowercase() != currentSport
                            TextButton(
                                onClick = {
                                    if (!isDisabled) {
                                        setCurrentSport(sportOption)
                                        onSportChanged(sportOption)
                                        setIsDialogOpen(false)
                                    }
                                },
                                enabled = !isDisabled
                            ) {
                                Text(
                                    sportOption.replaceFirstChar { it.uppercaseChar() },
                                    color = if (isDisabled) Color.Gray else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { setIsDialogOpen(false) }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
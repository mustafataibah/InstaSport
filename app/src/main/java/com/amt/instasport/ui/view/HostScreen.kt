package com.amt.instasport.ui.view

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amt.instasport.model.Event
import com.amt.instasport.model.SportsInterest
import com.amt.instasport.util.DateTransformation
import com.amt.instasport.viewmodel.AuthViewModel
import com.amt.instasport.viewmodel.EventsViewModel
import com.amt.instasport.viewmodel.UserDataViewModel
import kotlin.random.Random


@Composable
fun HostScreen(
    navController: NavController? = null,
    authViewModel: AuthViewModel? = null,
    eventsViewModel: EventsViewModel? = null,
    userDataViewModel: UserDataViewModel? = null
) {
    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var numCapacity by remember { mutableIntStateOf(0) }
    var selectedSport by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val userName = userName(userDataViewModel = userDataViewModel!!)
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Event Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        Row () {
            OutlinedTextField(
                value = eventLocation,
                onValueChange = { eventLocation = it },
                label = { Text("Location") },
                modifier = Modifier.weight(2f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.padding(8.dp))
            OutlinedTextField(
                value = numCapacity.toString(),
                onValueChange = { newValue -> numCapacity = newValue.toIntOrNull() ?: 0 },
                label = { Text("Participants") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        ExposedDropdownMenu(onSportSelected = { sport -> selectedSport = sport })

        Row(){
            dateText(onTextChanged = { text -> date = text })
        }

        descriptionBox(onTextChanged = { text -> description = text })

        Button(
            onClick = {

                val uid = authViewModel?.getCurrentUserId()

                if (uid != null) {
                    val sportsInterest = SportsInterest(
                        interestID = "${System.currentTimeMillis()}_${uid}_interest",
                        sportName = selectedSport
                    )
                    val event =
                        Event(
                            eventId = "${System.currentTimeMillis()}_${uid}_event",
                            hostUserId = uid,
                            hostUserName = userName,
                            title = eventName,
                            sportType = sportsInterest,
                            eventLocation = eventLocation,
                            dateTime = date,
                            maxParticipants = numCapacity,
                            description = description,
                            eventDistance = Random.nextDouble(0.1, 6.0),
                            level = "Expert")


                    eventsViewModel?.uploadEventsData(event)

                    navController?.navigate("events")
                    Log.d("Hostscreen","Event created")
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Create Event")
        }
    }


}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenu(onSportSelected: (String)->Unit) {
    val context = LocalContext.current
    val sports = arrayOf("Badminton", "Basketball", "Football", "Soccer", "Volleyball", "Tennis")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(sports[0]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Sport") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                sports.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            onSportSelected(item)
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun descriptionBox(
    onTextChanged: (String) -> Unit,
    initialText: String = "",
    maxWords: Int = 100
) {
    var text by remember { mutableStateOf(initialText) }

    OutlinedTextField(
        value = text,
        onValueChange = {
            if (it.count { char -> char.isWhitespace() } < maxWords) {
                text = it
                onTextChanged(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        label = { Text("Enter event description (50-100 words)") },
        minLines = 3,
        maxLines = 5,
        isError = text.count { char -> char.isWhitespace() } > maxWords,
        singleLine = false
    )
}

@Composable
fun dateText(
    onTextChanged: (String) -> Unit,
    initialText: String = "",
    maxChar: Int = 8
    ) {
    val maxChar = 8
    var text by remember { mutableStateOf(initialText) }
    OutlinedTextField(
        singleLine = true,
        value = text,
        onValueChange = {
            if (it.length <= maxChar) text = it
            onTextChanged(it)
        },
        label = { Text("Event Date") },
        visualTransformation = DateTransformation()
    )
}

@Composable
fun userName(userDataViewModel: UserDataViewModel): String {
    val currentUser by userDataViewModel.currentUserData.observeAsState()
    return currentUser?.name ?: "No name"
}

@Preview(showBackground = true)
@Composable
fun PreviewHostScreen() {
    HostScreen()
}

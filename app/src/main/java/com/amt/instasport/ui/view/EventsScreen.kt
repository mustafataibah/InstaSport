package com.amt.instasport.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun EventsScreen(navController: NavController? = null) {
    val eventsList = listOf("Pickup Basketball", "Local Tennis Match", "Volleyball Game", "Soccer Game")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(eventsList) { event ->
                EventItem(event)
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun EventItem(eventName: String) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        modifier = Modifier.padding(
            start = 4.dp,
            end = 4.dp,
            top = 6.dp,
            bottom = 2.dp,
        )
    ) {
        Text(
            text = eventName,
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 12.dp,
                )
        )
        Divider()
        Text(
            text = "Description",
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 12.dp,
                )
        )
        Row (modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(
                    start = 10.dp,
                    end = 0.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                )
            ) {
                Text (
                    text = "Info"
                )
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(
                    start = 10.dp,
                    end = 0.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                )
            ) {
                Text (
                    text = "Join"
                )
            }
        }
        Spacer(modifier = Modifier.padding(2.dp))
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewEventsScreen() {
    EventsScreen()
}
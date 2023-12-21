package com.amt.instasport.ui.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amt.instasport.R
import com.amt.instasport.model.Event
import com.amt.instasport.model.SportsInterest
import com.amt.instasport.ui.component.MapComposable
import com.amt.instasport.ui.theme.InstaSportFont
import com.amt.instasport.util.sportIconMap
import com.amt.instasport.viewmodel.UserDataViewModel

@Composable
fun DashboardScreen(navController: NavController, userDataViewModel: UserDataViewModel) {
    val currentUser by userDataViewModel.currentUserData.observeAsState()
    val mySports = currentUser?.followedSports?.map { sport ->
        sport.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }?.sortedBy { it.first() }

    val eventsList = listOf(
        Event("test1", "serena.w", "Serena Williams", "Tennis Match", SportsInterest("tennis", "tennis"), "", 1.0, "12/21/23 6:30 PM", 4,"Let's play a few friendly sets!", "intermediate"),
        Event("test2", "ricardo.s", "Ricardo Souza", "Volleyball Game", SportsInterest("volleyball", "volleyball"), "", 1.2, "12/22/23 5:00 PM", 6, "Come play volleyball with my friends and I!", "beginner"),
        Event("test4", "barney.d", "Barney D.", "Casual Badminton Match", SportsInterest("badminton", "badminton"), "", 0.1, "12/24/23 6:00 PM", 2, "Looking for a few people to play a casual game of badminton!", "beginner"),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        item { GreetingSection(currentUser?.name) }
        item { MySportsSection(navController, mySports) }
        item { EventsSection(navController, eventsList) }
    }
}

@Composable
fun GreetingSection (name: String?) {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.padding())
        Text(
            text = "Hello $name \uD83D\uDC4B ",
            fontSize = 24.sp,
            fontFamily = InstaSportFont,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Divider(modifier = Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(
                constraints.copy(
                    maxWidth = constraints.maxWidth + 32.dp.roundToPx(),
                )
            )
            layout(placeable.width, placeable.height) {
                placeable.place(0, 0)
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySportsSection (navController: NavController, mySports: List<String>?) {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "My Sports",
            fontSize = 20.sp,
            fontFamily = InstaSportFont,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        constraints.copy(
                            maxWidth = constraints.maxWidth + 32.dp.roundToPx(),
                        )
                    )
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                },
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(mySports ?: listOf()) { sport ->
                val displaySport =
                    sport.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                SportsItem(displaySport)
            }
            item {
                Column(
                    modifier = Modifier.padding(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ElevatedCard(
                        modifier = Modifier.size(85.dp),
                        shape = RoundedCornerShape(28.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        onClick = { navController.navigate("profile") }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Edit",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                    Text(
                        text = "Edit", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
            }
        }

        Divider(modifier = Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(
                constraints.copy(
                    maxWidth = constraints.maxWidth + 32.dp.roundToPx(),
                )
            )
            layout(placeable.width, placeable.height) {
                placeable.place(0, 0)
            }
        })
    }
}

@Composable
fun EventsSection (navController: NavController, eventsList: List<Event>) {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "Recommended Events",
            fontSize = 20.sp,
            fontFamily = InstaSportFont,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        constraints.copy(
                            maxWidth = constraints.maxWidth + 32.dp.roundToPx(),
                        )
                    )
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                },
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(eventsList) { event ->
                DashboardEventItem(navController, event)
            }
        }

        Spacer(modifier = Modifier.padding(2.dp))

        Text(
            text = "Events Near Me",
            fontSize = 20.sp,
            fontFamily = InstaSportFont,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        ElevatedCard(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .height(200.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
        ) {
            MapComposable()
        }
    }
}

@Composable
fun SportsItem(sport: String) {
    val iconResId = sportIconMap[sport.lowercase()] ?: R.drawable.sportsicon

    Column(
        modifier = Modifier.padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ElevatedCard(
            modifier = Modifier.size(85.dp),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "icon",
                    modifier = Modifier.size(85.dp)
                )
            }
        }
        Text(
            text = sport, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardEventItem(navController: NavController, event: Event) {
    val (
        eventId: String,
        hostUserId: String,
        hostUserName: String,
        title: String,
        sportType: SportsInterest,
        eventLocation: String,
        eventDistance: Double,
        dateTime: String,
        maxParticipants: Int,
        description: String,
        level: String
    ) = event

    ElevatedCard(
        modifier = Modifier.size(250.dp, 112.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        onClick = { navController.navigate("events") }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.instasport_logo),
                        contentDescription = "User Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = hostUserName,
                    fontFamily = InstaSportFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = title,
                fontFamily = InstaSportFont,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    DashboardScreen()
}
*/
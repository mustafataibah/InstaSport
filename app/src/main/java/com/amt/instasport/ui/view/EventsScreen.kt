package com.amt.instasport.ui.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amt.instasport.R
import com.amt.instasport.model.Event
import com.amt.instasport.model.SportsInterest
import com.amt.instasport.ui.theme.InstaSportFont
import com.amt.instasport.viewmodel.UserDataViewModel

enum class EventTab {
    AllEvents, MySports
}

@Composable
fun EventsScreen(userDataViewModel: UserDataViewModel, initialEventId: String? = null) {
    val currentUser by userDataViewModel.currentUserData.observeAsState()
    val mySports = currentUser?.followedSports?.map { sport ->
        sport.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }?.sortedBy { it.first() }

    val eventsList = listOf(
        Event("test1", "serena.w", "Serena Williams", "Tennis Match", SportsInterest("tennis", "tennis"), "Boston, MA", 1.0, "12/21/23 6:30 PM", 4,"Let's play a few friendly sets!", "intermediate"),
        Event("test2", "ricardo.s", "Ricardo Souza", "Volleyball Game", SportsInterest("volleyball", "volleyball"), "Boston, MA", 1.2, "12/22/23 5:00 PM", 6, "Come play volleyball with my friends and I!", "beginner"),
        Event("test3", "cristiano.r", "Christiano Ronaldo", "Recreational Soccer", SportsInterest("soccer", "soccer"), "Boston, MA", 1.3, "12/23/23 4:00 PM", 10, "Join us in a quick soccer game", "all"),
        Event("test4", "barney.d", "Barney D.", "Casual Badminton Match", SportsInterest("badminton", "badminton"), "Boston, MA", 0.1, "12/24/23 6:00 PM", 2, "Looking for a few people to play a casual game of badminton!", "beginner"),
        Event("test5", "patrick.s", "Patrick S.", "Competitive Volleyball Game", SportsInterest("volleyball", "volleyball"), "", 0.3, "12/25/23 7:00 PM", 8, "Come join me in a competitive volleyball match (must be at least intermediate skill)", "intermediate"),
        Event("test6", "charlie.b", "Charlie B.", "Local Football Match", SportsInterest("football", "football"), "Boston, MA", 0.6, "12/26/23 3:30 PM", 12, "Let's play a quick game of football, any locals welcome!", "all"),
        Event("test7", "jayson.t", "Jayson Tatum", "Pickup Basketball", SportsInterest("basketball", "basketball"), "Boston, MA", 0.7, "12/27/23 5:45 PM", 5, "Come play basketball with me!", "all"),

        // Chat GPT generated
        Event("test8", "marta.v", "Marta Vieira", "Intense Football Training", SportsInterest("football", "football"), "Boston, MA", 0.9, "12/28/23 2:00 PM", 15, "Intensive training session for football enthusiasts!", "advanced"),
        Event("test9", "gabriel.g", "Gabriel Garcia", "Beach Volleyball", SportsInterest("volleyball", "volleyball"), "Boston, MA", 1.5, "12/29/23 3:30 PM", 10, "Join us for a fun beach volleyball game at the seaside!", "all"),
        Event("test10", "diana.t", "Diana Taurasi", "Community Basketball Meetup", SportsInterest("basketball", "basketball"), "Boston, MA", 0.5, "12/30/23 4:15 PM", 8, "Everyone's welcome to our friendly neighborhood basketball meetup!", "all"),
        Event("test11", "lin.d", "Lin Dan", "Badminton Singles Challenge", SportsInterest("badminton", "badminton"), "Boston, MA", 0.4, "01/01/24 5:00 PM", 2, "Challenge yourself in a one-on-one badminton match!", "advanced"),
        Event("test12", "roger.f", "Roger Federer", "Weekend Tennis Club", SportsInterest("tennis", "tennis"), "Boston, MA", 0.8, "01/02/24 10:00 AM", 6, "Weekend tennis club for intermediate players looking to improve their game.", "intermediate"),
        Event("test13", "ali.f", "Ali Farag", "Squash Skills Workshop", SportsInterest("squash", "squash"), "Boston, MA", 1.1, "01/03/24 6:00 PM", 4, "Join our workshop to hone your squash skills with professional coaching.", "beginner")
    )

    var selectedTab by remember { mutableStateOf(EventTab.AllEvents) }
    var selectedEvent by remember { mutableStateOf(initialEventId) }

    /*
    TODO: trying to auto-scroll to selected event card
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(initialEventId) {
        initialEventId?.let { eventId ->
            val index = eventsList.indexOfFirst { it.eventId == eventId }
            if (index != -1) {
                coroutineScope.launch {
                    while (listState.layoutInfo.visibleItemsInfo.isEmpty()) {
                        delay(10)
                    }
                    listState.animateScrollToItem(index)
                }
            }
        }
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            indicator = { tabPositions ->
                CustomTabIndicator(tabPositions[selectedTab.ordinal])
            },
            divider = { },
            containerColor = MaterialTheme.colorScheme.secondary,
        ) {
            Tab(
                selected = selectedTab == EventTab.AllEvents,
                onClick = { selectedTab = EventTab.AllEvents },
                text = { Text("All Events") },
                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Tab(
                selected = selectedTab == EventTab.MySports,
                onClick = { selectedTab = EventTab.MySports },
                text = { Text("My Sports") },
                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Spacer(modifier = Modifier.padding(8.dp))
            }
            items(eventsList.sortedBy { it.eventDistance }) { event ->
                when (selectedTab) {
                    EventTab.AllEvents -> EventItem(
                        event = event,
                        isExpanded = selectedEvent == event.eventId,
                        onExpand = { expanded ->
                            selectedEvent = if (expanded) event.eventId else null
                        }
                    )
                    EventTab.MySports -> {
                        if (mySports?.contains(event.sportType.sportName.replaceFirstChar { it.uppercaseChar() }) == true) {
                            EventItem(
                                event = event,
                                isExpanded = selectedEvent == event.eventId,
                                onExpand = { expanded ->
                                    selectedEvent = if (expanded) event.eventId else null
                                }
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun CustomTabIndicator(tabPosition: TabPosition) {
    val indicatorWidth = tabPosition.width * 0.8f
    val indicatorOffset = tabPosition.left + (tabPosition.width - indicatorWidth) / 2

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = indicatorOffset)
            .width(indicatorWidth)
            .height(3.dp)
            .background(
                color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(1.5.dp)
            )
    )
}

@Composable
fun EventItem(event: Event, isExpanded: Boolean, onExpand: (Boolean) -> Unit) {
    val (_, _, hostUserName: String, title: String, _, _, eventDistance: Double, _, _, description: String, _) = event

    var localExpanded by remember { mutableStateOf(isExpanded) }
    var joinEnabled by remember { mutableStateOf(true) }

    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        AlertDialog(
            title = {
                Column {
                    Text(
                        text = event.title,
                        fontFamily = InstaSportFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Divider()
                }
            },
            text = {
                Column {
                    Column {
                        Text(
                            text = "Sport",
                            fontFamily = InstaSportFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                        Spacer(modifier = Modifier.padding(1.dp))
                        Text(
                            text = event.sportType.sportName.replaceFirstChar { it.uppercaseChar() },
                            fontFamily = InstaSportFont,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Column {
                        Text(
                            text = "Description",
                            fontFamily = InstaSportFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                        Spacer(modifier = Modifier.padding(1.dp))
                        Text(
                            text = event.description,
                            fontFamily = InstaSportFont,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Column {
                        Text(
                            text = "Location",
                            fontFamily = InstaSportFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                        Spacer(modifier = Modifier.padding(1.dp))
                        Text(
                            text = event.eventLocation,
                            fontFamily = InstaSportFont,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Column {
                        Text(
                            text = "Date",
                            fontFamily = InstaSportFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                        Spacer(modifier = Modifier.padding(1.dp))
                        Text(
                            text = event.dateTime,
                            fontFamily = InstaSportFont,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    }
                }
            },
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = { showDialog.value = false } ) {
                    Text("OK")
                }
            },
            dismissButton = {}
        )
    }

    LaunchedEffect(isExpanded) {
        localExpanded = isExpanded
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) MaterialTheme.colorScheme.secondary else Color.White
        ),
        border = if (isExpanded) null else BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                localExpanded = !localExpanded
                onExpand(localExpanded)
            }
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
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$eventDistance mi away",
                    fontFamily = InstaSportFont,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = InstaSportFont,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (localExpanded) {
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = description,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = InstaSportFont,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.padding(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = { showDialog.value = true },
                        modifier = Modifier.padding(
                        )
                    ) {
                        Text(
                            text = "Info"
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Button(
                        onClick = { joinEnabled = false },
                        enabled = joinEnabled,
                        modifier = Modifier.padding()
                    ) {
                        when {
                            (joinEnabled) -> Text(text = "Join")
                            else -> Text(text = "Joined")
                        }
                        /* TODO: add to database, remember joined state */
                    }
                }
            }
        }
    }
}

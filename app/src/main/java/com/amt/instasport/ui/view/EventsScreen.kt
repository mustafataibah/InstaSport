package com.amt.instasport.ui.view

import android.util.Log
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
import com.amt.instasport.ui.theme.InstaSportFont
import com.amt.instasport.viewmodel.EventsViewModel
import com.amt.instasport.viewmodel.UserDataViewModel

enum class EventTab {
    AllEvents, MySports
}

@Composable
fun EventsScreen(
    userDataViewModel: UserDataViewModel,
    initialEventId: String? = null,
    eventsViewModel: EventsViewModel
) {
    val allEvents by eventsViewModel.allEvents.observeAsState(listOf())
    Log.d("Events", "allEvents: $allEvents")
    val currentUser by userDataViewModel.currentUserData.observeAsState()
    val mySports = currentUser?.followedSports?.map { sport ->
        sport.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }?.sortedBy { it.first() }
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
        modifier = Modifier.fillMaxSize(),
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
            items(allEvents.sortedBy { it.eventDistance }) { event ->
                when (selectedTab) {
                    EventTab.AllEvents -> EventItem(event = event,
                        isExpanded = selectedEvent == event.eventId,
                        onExpand = { expanded ->
                            selectedEvent = if (expanded) event.eventId else null
                        })

                    EventTab.MySports -> {
                        if (mySports?.contains(event.sportType.sportName.replaceFirstChar { it.uppercaseChar() }) == true) {
                            EventItem(event = event,
                                isExpanded = selectedEvent == event.eventId,
                                onExpand = { expanded ->
                                    selectedEvent = if (expanded) event.eventId else null
                                })
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
        AlertDialog(title = {
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
        }, text = {
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
        }, onDismissRequest = { showDialog.value = false }, confirmButton = {
            TextButton(onClick = { showDialog.value = false }) {
                Text("OK")
            }
        }, dismissButton = {})
    }

    LaunchedEffect(isExpanded) {
        localExpanded = isExpanded
    }

    Card(colors = CardDefaults.cardColors(
        containerColor = if (isExpanded) MaterialTheme.colorScheme.secondary else Color.White
    ),
        border = if (isExpanded) null else BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                localExpanded = !localExpanded
                onExpand(localExpanded)
            }) {
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
                        onClick = { showDialog.value = true }, modifier = Modifier.padding(
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
                        }/* TODO: add to database, remember joined state */
                    }
                }
            }
        }
    }
}

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amt.instasport.R


data class EventData(
    val title: String,
    val description: String,
    val author: String,
    val distance: Double,
)

enum class EventTab {
    AllEvents, MySports
}

@Composable
fun EventsScreen() {
    val eventsList = listOf(
        EventData("Pickup Basketball", "Come play basketball with me!", "Jayson Tatum", 0.4),
        EventData("Tennis Match", "Let's play a few friendly sets!", "Serena Williams", 0.7),
        EventData(
            "Volleyball Game", "Come play volleyball with my friends and I!", "Ricardo Souza", 1.2
        ),
        EventData(
            "Recreational Soccer", "Join us in a quick soccer game", "Christiano Ronaldo", 1.3
        ),
    )

    var selectedTab by remember { mutableStateOf(EventTab.AllEvents) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            indicator = { tabPositions ->
                CustomTabIndicator(tabPositions[selectedTab.ordinal])
            },
            divider = { },
            containerColor = Color.Transparent,
            modifier = Modifier.padding(vertical = 8.dp)
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
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(eventsList) { event ->
                EventItem(event)
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
fun EventItem(event: EventData) {
    val (title, description, author, distance) = event
    var isExpanded by remember { mutableStateOf(false) }

    Card(colors = CardDefaults.cardColors(
        containerColor = if (isExpanded) MaterialTheme.colorScheme.secondaryContainer else Color.White
    ),
        border = if (isExpanded) null else BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { isExpanded = !isExpanded }) {
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
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = author,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$distance mi away", modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = title,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.padding(2.dp))

            if (isExpanded) {
                Text(
                    text = description, modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = { /*TODO*/ }, modifier = Modifier.padding(
                        )
                    ) {
                        Text(
                            text = "Info"
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Button(
                        onClick = { /*TODO*/ }, modifier = Modifier.padding(
                        )
                    ) {
                        Text(
                            text = "Join"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEventsScreen() {
    EventsScreen()
}
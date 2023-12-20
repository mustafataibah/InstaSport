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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
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
import com.amt.instasport.R
import com.amt.instasport.ui.component.MapComposable
import com.amt.instasport.ui.theme.InstaSportFont
import com.amt.instasport.util.sportIconMap
import com.amt.instasport.viewmodel.UserDataViewModel

@Composable
fun DashboardScreen(userDataViewModel: UserDataViewModel) {
    val currentUser by userDataViewModel.currentUserData.observeAsState()
    val mySports = currentUser?.followedSports?.map { sport ->
        sport.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

    val eventsList = listOf(
        EventData("Casual Badminton Match", "", "Barney D.", 0.4),
        EventData("Competitive Volleyball Game", "", "Patrick S.", 0.7),
        EventData("Local Soccer Match", "", "Charlie B.", 1.2),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Hello ${currentUser?.name}",
            fontSize = 20.sp,
            fontFamily = InstaSportFont,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp)
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
        Text(
            text = "My Sports",
            fontSize = 20.sp,
            fontFamily = InstaSportFont,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 8.dp)
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
                        placeable.place(50, 0)
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

        Text(
            text = "Recommended Events",
            fontSize = 20.sp,
            fontFamily = InstaSportFont,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp)
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
                DashboardEventItem(event)
            }
        }

        Spacer(modifier = Modifier.padding(2.dp))

        Text(
            text = "Events Near Me",
            fontSize = 20.sp,
            fontFamily = InstaSportFont,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp)
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
            /* TODO: maybe can implement Google Maps API here */
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

@Composable
fun DashboardEventItem(event: EventData) {
    val (title, _, author, _) = event

    ElevatedCard(
        modifier = Modifier.size(200.dp, 116.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
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
                    text = author,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 18.sp,
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = title,
                style = TextStyle(fontWeight = FontWeight.Normal),
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
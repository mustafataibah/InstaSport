package com.amt.instasport

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amt.instasport.ui.view.EventData
import com.amt.instasport.viewmodel.UserDataViewModel

data class SportItemData(
    val sport: String,
    val icon: Int
)

@Composable
fun DashboardScreen(navController: NavController? = null, userDataViewModel: UserDataViewModel) {
    val currentUser by userDataViewModel.currentUserData.observeAsState()

    /* TODO: add vector graphics for sports */
    val sportsList = listOf(
        SportItemData("Tennis", R.drawable.tennis),
        SportItemData("Volleyball", R.drawable.volleyball),
        SportItemData("Badminton", R.drawable.badminton),
        SportItemData("Basketball", R.drawable.basketball),
    ).sortedBy { it.sport.first() }

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

        Text (
            text = "Hello ${currentUser?.name}",
            //text = "Hello Andrew",
            fontSize = 20.sp,
            style = TextStyle(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Divider(modifier = Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints.copy(
                maxWidth = constraints.maxWidth + 32.dp.roundToPx(),
            ))
            layout(placeable.width, placeable.height) {
                placeable.place(0, 0)
            }
        })
        Text (
            text = "My Sports",
            fontSize = 20.sp,
            style = TextStyle(fontWeight = FontWeight.Normal),
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
            items(sportsList) { sportItem ->
                SportsItem(sportItem)
            }
        }

        Divider(modifier = Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints.copy(
                maxWidth = constraints.maxWidth + 32.dp.roundToPx(),
            ))
            layout(placeable.width, placeable.height) {
                placeable.place(0, 0)
            }
        })

        Text (
            text = "Recommended Events",
            fontSize = 20.sp,
            style = TextStyle(fontWeight = FontWeight.SemiBold),
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
                EventItem(event)
            }
        }

        Spacer(modifier = Modifier.padding(2.dp))

        Text (
            text = "Events Near Me",
            fontSize = 20.sp,
            style = TextStyle(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        ElevatedCard (
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .height(200.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
        ) {
            /* TODO: maybe can implement Google Maps API here */
        }
    }
}

@Composable
fun SportsItem(sportItem: SportItemData) {
    Column (
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
                Icon(
                    painter = painterResource(id = sportItem.icon),
                    contentDescription = sportItem.sport,
                    modifier = Modifier.scale(0.25f)
                )
            }
        }
        Text (text = sportItem.sport,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun EventItem (event: EventData) {
    val (title, _, author, _) = event

    ElevatedCard (
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
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
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
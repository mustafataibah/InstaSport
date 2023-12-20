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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
    val mySports = currentUser?.followedSports?.map { sport ->
        sport.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
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
                Text(
                    text = user.name,
                    fontFamily = InstaSportFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
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
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        Divider()

        Spacer(modifier = Modifier.height(4.dp))

        // Skill Blocks
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "My Sports",
                fontFamily = InstaSportFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
            )
            mySports?.forEach { sport ->
                SportsBlock(sport = sport)
            }
        }
    }
}

@Composable
fun SportsBlock(sport: String) {
    val iconResId = sportIconMap[sport.lowercase()] ?: R.drawable.sportsicon

    Row(
        modifier = Modifier
            .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = "icon",
            modifier = Modifier.size(60.dp)
        )
        Text(
            text = sport, fontSize = 22.sp, fontWeight = FontWeight.Medium
        )
    }
}
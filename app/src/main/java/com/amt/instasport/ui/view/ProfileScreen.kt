package com.amt.instasport

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
import androidx.navigation.NavController
import com.amt.instasport.viewmodel.UserDataViewModel

@Composable
fun ProfileScreen(navController: NavController? = null, userDataViewModel: UserDataViewModel) {
    val currentUser by userDataViewModel.currentUserData.observeAsState()
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
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "User Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            currentUser?.let { user ->
                Text(
                    text = user.name, fontWeight = FontWeight.Bold
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Icon(
                    Icons.Filled.Place,
                    contentDescription = "Location",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Boston, MA", modifier = Modifier.padding(end = 8.dp))
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
            Text(text = "My Sports")
            SkillBlock(skill = "Badminton", level = "Expert")
            SkillBlock(skill = "Basketball", level = "Beginner")
            SkillBlock(skill = "Volleyball", level = "Master")
        }
    }
}


@Composable
fun SkillBlock(skill: String, level: String) {
    Box(
        modifier = Modifier
            .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center
        ) {
            Text(text = skill, fontWeight = FontWeight.Bold)
            Text(text = level)
        }
    }
}



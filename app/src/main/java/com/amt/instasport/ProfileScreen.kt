package com.amt.instasport

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
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

        Text(
            text = "Android Robot",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Boston, MA")

        Spacer(modifier = Modifier.height(16.dp))

        // Skill Blocks
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = skill, fontWeight = FontWeight.Bold)
            Text(text = level)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen()
}

package com.amt.instasport.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.amt.instasport.R
import com.amt.instasport.ui.component.ButtonsSection
import com.amt.instasport.ui.component.PageIndicator
import com.amt.instasport.ui.theme.InstaSportFont
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navController: NavHostController) {
    val animations = listOf(
        R.drawable.onboarding1, R.drawable.onboarding2, R.drawable.onboarding3
    )
    val titles = listOf(
        "Welcome to the Game", "Join the Action", "Host Your Game"
    )
    val descriptions = listOf(
        "Join our sports community and find your perfect game.",
        "Easily find and participate in local sports events.",
        "Organize your own sports events and invite players."
    )
    val pagerState = rememberPagerState(
        pageCount = animations.size
    )

    Column(
        Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalPager(
            state = pagerState, Modifier.wrapContentSize()
        ) { currentPage ->
            Column(
                Modifier
                    .wrapContentSize()
                    .padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = animations[currentPage]),
                    contentDescription = "User Avatar",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(40.dp))
                Text(
                    text = titles[currentPage],
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Center,
                    fontSize = 44.sp,
                    fontFamily = InstaSportFont,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = descriptions[currentPage],
                    Modifier.padding(top = 45.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontFamily = InstaSportFont,
                    fontWeight = FontWeight.Light,
                )
            }
        }

        PageIndicator(
            pageCount = animations.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.padding(60.dp)
        )
    }

    ButtonsSection(
        pagerState = pagerState, navController = navController
    )

}

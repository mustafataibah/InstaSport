package com.amt.instasport.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amt.instasport.R
import com.amt.instasport.ui.component.ButtonsSection
import com.amt.instasport.ui.component.PageIndicator
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navController: NavHostController) {
    val animations = listOf(
        R.raw.animation, R.raw.animation, R.raw.animation
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
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animations[currentPage]))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(400.dp)
                )
                Text(
                    text = titles[currentPage],
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Center,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = descriptions[currentPage],
                    Modifier.padding(top = 45.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp

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

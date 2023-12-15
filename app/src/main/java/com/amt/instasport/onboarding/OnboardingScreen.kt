package com.amt.instasport.onboarding

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.amt.instasport.MainActivity
import com.amt.instasport.R
import com.amt.instasport.ui.theme.Lavender
import com.amt.instasport.ui.theme.SecondPurple
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navController: NavHostController, context: MainActivity) {
    // Using these animations I got online for now, will change them later
    val animations = listOf(
        R.raw.animation, R.raw.animation, R.raw.animation
    )
    val titles = listOf(
        "Welcome", "Host", "Join"
    )

    val descriptions = listOf(
        "Description 1", "Description 2", "Description 3"
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
        pagerState = pagerState, navController = navController, context = context
    )

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ButtonsSection(
    pagerState: PagerState,
    navController: NavHostController,
    context: MainActivity
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        if (pagerState.currentPage == 2) {
            OutlinedButton(
                onClick = {
                    onBoardingIsFinished(context = context)
                    navController.popBackStack()
                    navController.navigate("landing")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Lavender
                ),
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier
    ) {
        repeat(pageCount) {
            IndicatorSingleDot(isSelected = it == currentPage)
        }


    }
}

@Composable
fun IndicatorSingleDot(isSelected: Boolean) {

    val width = animateDpAsState(targetValue = if (isSelected) 35.dp else 15.dp, label = "")
    Box(
        modifier = Modifier
            .padding(2.dp)
            .height(15.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(if (isSelected) Lavender else SecondPurple)
    )
}

private fun onBoardingIsFinished(context: MainActivity) {
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isFinished", true)
    editor.apply()

}

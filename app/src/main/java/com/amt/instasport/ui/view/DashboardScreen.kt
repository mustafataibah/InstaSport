package com.amt.instasport

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


import coil.compose.AsyncImage
import kotlinx.coroutines.launch

/*
val images = listOf(
    "https://cdn.pixabay.com/photo/2016/05/31/23/21/badminton-1428046_1280.jpg",
    "https://media.istockphoto.com/id/1136997398/photo/blocking-an-offensive-player.jpg?s=1024x1024&w=is&k=20&c=p25an8WHz8CxoeZiP_UIChQ5D9A4fzinMmuUeQUB_Ck=",
    "https://cdn.pixabay.com/photo/2015/05/26/00/48/basketball-784097_1280.jpg",
    "https://cdn.pixabay.com/photo/2020/11/27/18/59/tennis-5782695_1280.jpg",
    "https://cdn.pixabay.com/photo/2023/05/11/03/34/baseball-7985433_1280.jpg"
)

@Composable
private fun CarouselSlider (images: List<String>) {
    var index by remember { mutableIntStateOf(0) }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true, block = {
        coroutineScope.launch {
            if (index == images.size - 1) index = 0
            else index++
            scrollState.animateScrollToItem(index)
        }
    })

    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        Box (modifier = Modifier.padding(0.dp)) {
            LazyRow (
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(images) {index, image ->
                    Card (
                        modifier = Modifier
                            .height(300.dp)
                            .padding(
                                start = 16.dp
                            ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        )
                    ) {
                        AsyncImage(
                            model = image,
                            contentDescription = "Image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.width(300.dp))
                    }
                }
            }
        }
    }
}
*/

@Composable
fun DashboardScreen(navController: NavController? = null) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(
                top = 16.dp,
                bottom = 16.dp,
                start = 0.dp,
                end = 0.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface (
            modifier = Modifier.fillMaxSize()
        ) {
            // CarouselSlider(images)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    DashboardScreen()
}

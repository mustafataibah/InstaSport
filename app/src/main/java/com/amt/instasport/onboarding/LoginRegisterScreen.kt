//@file:OptIn(ExperimentalMaterial3Api::class)
//
//package com.amt.instasport.onboarding
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.text.ClickableText
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.amt.instasport.R
//
////@Composable
////fun LoginRegisterScreen(navController: NavController? = null) {
////    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
////
////    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
////        CenterAlignedTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
////            containerColor = MaterialTheme.colorScheme.primaryContainer,
////            titleContentColor = MaterialTheme.colorScheme.primary,
////        ), title = { Text("Sign Up") }, navigationIcon = {
////            IconButton(onClick = { navController?.navigateUp() }) {
////                Icon(
////                    imageVector = Icons.Filled.ArrowBack, contentDescription = "Back"
////                )
////            }
////        }, scrollBehavior = scrollBehavior
////        )
////    }) { innerPadding ->
////        LoginRegisterScreenContent(navController, innerPadding)
////    }
////}
//
//@Composable
//fun LoginRegisterScreen(navController: NavController? = null) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        val logoPainter = painterResource(id = R.drawable.instasport_logo)
//        val typoPainter = painterResource(id = R.drawable.typography)
//
//        Spacer(modifier = Modifier.weight(2f))
//
//        Image(
//            painter = logoPainter, contentDescription = "Logo", modifier = Modifier.weight(1f)
//        )
//
//        Image(
//            painter = typoPainter,
//            contentDescription = "Typography",
//            modifier = Modifier
//                .fillMaxWidth(0.6f)
//                .weight(0.6f)
//        )
//
//        // Buttons and clickable text
//        Column(
//            modifier = Modifier.weight(2f),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Button(
//                onClick = {
//                    navController?.navigate("register")
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
//                modifier = Modifier
//                    .width(220.dp)
//                    .height(50.dp)
//            ) {
//                Text("Sign Up with Phone")
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            val annotatedText = buildAnnotatedString {
//                withStyle(style = SpanStyle(color = Color.Gray)) {
//                    append("Already a user? ")
//                }
//                pushStringAnnotation(tag = "LOG_IN", annotation = "login")
//                withStyle(
//                    style = SpanStyle(
//                        textDecoration = TextDecoration.Underline, color = Color.Gray
//                    )
//                ) {
//                    append("Sign In")
//                }
//                pop()
//            }
//
//            ClickableText(text = annotatedText, onClick = { offset ->
//                annotatedText.getStringAnnotations(tag = "LOG_IN", start = offset, end = offset)
//                    .firstOrNull()?.let { navController?.navigate("login") }
//            })
//        }
//
//        Spacer(modifier = Modifier.weight(2f))
//
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewLoginRegisterScreen() {
//    LoginRegisterScreen()
//}
//

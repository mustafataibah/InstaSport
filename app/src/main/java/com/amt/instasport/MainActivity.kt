package com.amt.instasport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.amt.instasport.ui.theme.InstaSportTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InstaSportTheme {
                AppNavigation()
            }
        }
    }
}

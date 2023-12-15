package com.amt.instasport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.amt.instasport.ui.theme.InstaSportTheme
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        setContent {
            InstaSportTheme {
                AppNavigation()
            }
        }
    }
}

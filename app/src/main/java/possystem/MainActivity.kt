package com.example.possystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.possystem.navigation.AppNavHost
import com.example.possystem.ui.theme.POSSystemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen() // ✅ correct

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            POSSystemTheme {
                AppNavHost()
            }
        }
    }
}
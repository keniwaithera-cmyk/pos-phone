package com.example.possystem.ui.theme.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.possystem.R
import com.example.possystem.navigation.ROUTE_DASHBOARD

@Composable
fun SplashScreen(navController: NavController) {

    var startAnimation by remember { mutableStateOf(false) }

    // Scale animation
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.6f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ), label = ""
    )

    // Fade animation
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1200), label = ""
    )

    LaunchedEffect(true) {
        startAnimation = true
        delay(2200)

        navController.navigate(ROUTE_DASHBOARD) {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(140.dp)
                    .scale(scale)
                    .alpha(alpha)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "POS System",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(alpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Loading...",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.alpha(alpha)
            )
        }
    }
}
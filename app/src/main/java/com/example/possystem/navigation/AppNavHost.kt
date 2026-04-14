package com.example.possystem.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.possystem.ui.theme.screens.dashboard.Dashboard
import com.example.possystem.ui.theme.screens.login.LoginScreen
import com.example.possystem.ui.theme.screens.register.RegisterScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_LOGIN // ✅ Start on Login, not Register
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(ROUTE_LOGIN) { LoginScreen(navController) }       // ✅ Fixed
        composable(ROUTE_REGISTER) { RegisterScreen(navController) } // ✅ Correct
        composable(ROUTE_DASHBOARD) { Dashboard(navController) }     // ✅ Correct
    }
}
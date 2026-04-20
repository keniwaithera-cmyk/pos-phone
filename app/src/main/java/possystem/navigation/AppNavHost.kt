package com.example.possystem.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.possystem.ui.theme.screens.dashboard.Dashboard
import com.example.possystem.ui.theme.screens.login.LoginScreen
import com.example.possystem.ui.theme.screens.product.AddProductScreen
import com.example.possystem.ui.theme.screens.product.ProductListScreen
import com.example.possystem.ui.theme.screens.register.RegisterScreen
import com.example.possystem.ui.theme.screens.splash.SplashScreen
import com.example.possystem.navigation.*
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_ADD_PRODUCT// ✅ FIXED
) {
    NavHost(navController, startDestination = startDestination) {

        composable(ROUTE_SPLASH) { SplashScreen(navController) }
        composable(ROUTE_LOGIN) { LoginScreen(navController) }
        composable(ROUTE_REGISTER) { RegisterScreen(navController) }
        composable(ROUTE_DASHBOARD) { Dashboard(navController) }
        composable(ROUTE_ADD_PRODUCT) { AddProductScreen(navController) }
        composable(ROUTE_VIEW_PRODUCT) { ProductListScreen(navController) }
    }
}
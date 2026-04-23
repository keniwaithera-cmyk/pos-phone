package com.example.possystem.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.possystem.ui.theme.screens.dashboard.Dashboard
import com.example.possystem.ui.theme.screens.login.LoginScreen
import com.example.possystem.ui.theme.screens.product.AddProductScreen
import com.example.possystem.ui.theme.screens.product.ProductListScreen
import com.example.possystem.ui.theme.screens.product.UpdateProductScreen
import com.example.possystem.ui.theme.screens.register.RegisterScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    // If user is already logged in → go to Dashboard, otherwise → Login
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) ROUTE_VIEW_PRODUCT else ROUTE_LOGIN

    NavHost(navController = navController, startDestination = startDestination) {
        composable(ROUTE_REGISTER) { RegisterScreen(navController) }
        composable(ROUTE_LOGIN) { LoginScreen(navController) }
        composable(ROUTE_DASHBOARD) { Dashboard(navController) }
        composable(ROUTE_ADD_PRODUCT) { AddProductScreen(navController) }
        composable(ROUTE_VIEW_PRODUCT) { ProductListScreen(navController) }
        composable(
            route = "$ROUTE_UPDATE_PRODUCT/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            UpdateProductScreen(navController = navController, productId = productId)
        }
    }
}
package com.example.possystem.ui.theme.screens.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.possystem.data.AuthViewModel
import com.example.possystem.navigation.ROUTE_ADD_PRODUCT
import com.example.possystem.navigation.ROUTE_VIEW_PRODUCT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("POS Dashboard", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Green),
                actions = {
                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { authViewModel.logout(navController, context) }
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.Blue) {
                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = { selectedItem = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate(ROUTE_ADD_PRODUCT)
                    },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add Product") },
                    label = { Text("Add") }
                )
                NavigationBarItem(
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        navController.navigate(ROUTE_VIEW_PRODUCT)
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = "Products") },
                    label = { Text("Products") }
                )
                NavigationBarItem(
                    selected = selectedItem == 3,
                    onClick = { selectedItem = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Business Overview",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            // Revenue Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Today's Revenue", color = Color.White, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "KES 12,500",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Quick Action Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate(ROUTE_ADD_PRODUCT) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+ New Product", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate(ROUTE_VIEW_PRODUCT) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("View Products", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Profit Graph
            val myData = listOf(4000f, 9000f, 6000f, 15000f, 10000f, 18000f, 14000f)
            ProfitGraph(profitData = myData)
        }
    }
}

@Composable
fun ProfitGraph(profitData: List<Float>) {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Weekly Profit Trend",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val maxProfit = profitData.maxOrNull() ?: 1f
                        val spaceX = width / (profitData.size - 1)

                        val strokePath = Path().apply {
                            for (i in profitData.indices) {
                                val x = i * spaceX
                                val y = height - (profitData[i] / maxProfit * height)
                                if (i == 0) moveTo(x, y) else lineTo(x, y)
                            }
                        }

                        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath()
                        fillPath.lineTo(width, height)
                        fillPath.lineTo(0f, height)
                        fillPath.close()

                        drawPath(
                            fillPath,
                            Brush.verticalGradient(listOf(Color(0xFF4CAF50).copy(alpha = 0.3f), Color.Transparent))
                        )
                        drawPath(
                            strokePath,
                            Color(0xFF2E7D32),
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    days.forEach { day ->
                        Text(text = day, fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    Dashboard(rememberNavController())
}
package com.example.possystem.ui.theme.screens.dashboard

import android.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.possystem.navigation.ROUTE_REGISTER
// For Scrolling
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

// For the Graph Drawing
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.possystem.data.AuthViewModel
import com.example.possystem.navigation.ROUTE_VIEW_PRODUCT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavController){
    var selectedItem by remember { mutableStateOf(0) }
    val authViewModel: AuthViewModel= viewModel()
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text(text ="POS Dashboard", fontWeight = FontWeight.Bold ) }
            , colors = TopAppBarDefaults.topAppBarColors(Color.Green),
            actions = {
                Text(
                    text = "LogOut",
                    fontSize = 20.sp,
                    color = Color.Red,
                    modifier = Modifier.clickable {
                        authViewModel.logout(navController, context)
                    }
                )
            })},
        bottomBar = { NavigationBar(containerColor = Color.Green){
            NavigationBarItem(
                selected = selectedItem == 0,
                onClick = { selectedItem = 0},
                icon = { Icon(Icons.Default.Home,null) },
                label = { Text("Home", color = Color.Black) }
            )
            NavigationBarItem(
                selected = selectedItem == 0,
                onClick = { selectedItem = 0},
                icon = { Icon(Icons.Default.Settings,null) },
                label = { Text("Settings", color = Color.Black) }
            )
            NavigationBarItem(
                selected = selectedItem == 0,
                onClick = { selectedItem = 0},
                icon = { Icon(Icons.Default.Person,null) },
                label = { Text("Person", color = Color.Black) }
            )
        } }
    ) {padding ->
        Column(modifier = Modifier.padding(padding).verticalScroll(rememberScrollState())){
            Text(text = "Business Overview",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Green),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Today's Revenue", color = Color.Black, fontSize = 20.sp)

                    Text(
                        "Kes 12,500",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                }

            }


            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),)
            {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Green),
                    elevation = CardDefaults.cardElevation(6.dp)
                ){
                    Box(modifier = Modifier.padding(12.dp),
                        contentAlignment = Alignment.Center)
                    {Text(text = "New Products", fontSize = 20.sp, color = Color.Black)}
                }

                Card(
                    modifier = Modifier.weight(1f).clickable{
                        navController.navigate(ROUTE_VIEW_PRODUCT)
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Green),
                    elevation = CardDefaults.cardElevation(6.dp)
                ){
                    Box(modifier = Modifier.padding(12.dp),
                        contentAlignment = Alignment.Center)
                    {Text(text = "Products", fontSize = 20.sp, color = Color.Black)}
                }





            }
            val myData = listOf(4000f, 9000f, 6000f, 15000f, 10000f, 18000f, 14000f)
            ProfitGraph(profitData = myData)

        }

    }

}

@Composable
fun ProfitGraph(profitData: List<Float>) {

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")


    Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
        Text(
            text = "Weekly Profit Trend",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
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
                            Brush.verticalGradient(listOf(Color.Green.copy(0.2f), Color.Transparent))
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
                    horizontalArrangement = Arrangement.SpaceBetween // Spreads days out evenly
                ) {
                    days.forEach { day ->
                        Text(
                            text = day,
                            fontSize = 10.sp, // Small font to ensure they all fit
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview(){
    Dashboard(rememberNavController())
}
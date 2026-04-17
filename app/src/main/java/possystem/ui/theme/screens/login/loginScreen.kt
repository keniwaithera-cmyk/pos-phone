package com.example.possystem.ui.theme.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.possystem.R
import com.example.possystem.data.AuthViewModel
import com.example.possystem.navigation.ROUTE_LOGIN
import com.example.possystem.navigation.ROUTE_REGISTER

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel= viewModel()
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()) {
        Image(
            painter = painterResource(id = R.drawable.airplane_bg),
            contentDescription = "Background of an airplane at sunset",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )




        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.airplane_bg),
                contentDescription = "logo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .shadow(10.dp, CircleShape),
                contentScale = ContentScale.Crop

            )

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = "Login Here",
                fontSize = 33.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(16.dp))

            androidx.compose.material3.Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(15.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                color = Color.White.copy(alpha = 0.85f)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp), // Give it some breathing room
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {


                    OutlinedTextField(
                        value = email,
                        label = {
                            Text(
                                text = "Enter Email", color = Color.Black
                            )
                        },
                        onValueChange = { email = it },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Red,
                            unfocusedTextColor = Color.Black,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        label = { Text(text = "Enter Password", color = Color.Black) },
                        onValueChange = { password = it },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Red,
                            unfocusedTextColor = Color.Black,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        )
                    )


                Spacer(modifier = Modifier.height(16.dp))

                Row() {
                    Text(
                        text = "Don't have an account?",
                        color = Color.Green
                    )

                    Text(
                        text = "Register Here", fontSize = 20.sp,
                        color = Color.Red, modifier = Modifier.clickable {
                            navController.navigate(
                                ROUTE_REGISTER
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    authViewModel.login(
                        email = email,
                        password = password,

                        navController = navController,
                        context = context
                    )
                }) { Text(text = "Login") }

            }
            }
        }
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview(){
    LoginScreen(rememberNavController())
}


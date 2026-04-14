package com.example.possystem.ui.theme.screens.register

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.possystem.R
import com.example.possystem.data.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }
    var phonenumber by remember { mutableStateOf("") }
    val animatedColor = animatedBackground()
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedColor)
            .padding(horizontal = 20.dp, vertical = 8.dp) // ✅ reduced vertical padding
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.ken),
            contentDescription = "ken",
            modifier = Modifier
                .size(80.dp) // ✅ reduced from 150dp
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp)) // ✅ reduced from 16dp

        Text(
            text = "Create Account",
            fontSize = 24.sp, // ✅ reduced from 33sp
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00C853)
        )

        Spacer(modifier = Modifier.height(12.dp)) // ✅ reduced from 16dp

        // ✅ Compact reusable field style - Username
        OutlinedTextField(
            value = username,
            label = { Text(text = "Username", fontSize = 12.sp) },
            onValueChange = { username = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(6.dp), // ✅ reduced from 8dp
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        )

        Spacer(modifier = Modifier.height(6.dp)) // ✅ reduced from 8dp

        // ✅ Email
        OutlinedTextField(
            value = email,
            label = { Text(text = "Email", fontSize = 12.sp) },
            onValueChange = { email = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(6.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        // ✅ Phone
        OutlinedTextField(
            value = phonenumber,
            label = { Text(text = "Phone number", fontSize = 12.sp) },
            onValueChange = { phonenumber = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(6.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        // ✅ Password
        OutlinedTextField(
            value = password,
            label = { Text(text = "Password", fontSize = 12.sp) },
            onValueChange = { password = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), // ✅ hides password text
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color(0xFF6A11CB), CircleShape)
                        .padding(6.dp),
                    tint = Color.White
                )
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        // ✅ Confirm Password
        OutlinedTextField(
            value = confirmpassword,
            label = { Text(text = "Confirm password", fontSize = 12.sp) },
            onValueChange = { confirmpassword = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), // ✅ hides password text
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color(0xFF6A11CB), CircleShape)
                        .padding(6.dp),
                    tint = Color.White
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp)) // ✅ reduced from 24dp

        Button(
            onClick = {
                authViewModel.signup(
                    username = username,
                    email = email,
                    password = password,
                    confirmpassword = confirmpassword,
                    phonenumber = phonenumber,
                    navController = navController,
                    context = context
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp) // ✅ fixed button height
        ) {
            Text(text = "Register", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Clickable login link
        Row {
            Text(text = "Already registered? ", color = Color.White, fontSize = 13.sp)
            Text(
                text = "Login here",
                color = Color(0xFF00C853),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun animatedBackground(): Color {
    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val color by infiniteTransition.animateColor(
        initialValue = Color(0xFF1565C0),
        targetValue = Color(0xFFE91E63),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )
    return color
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(rememberNavController())
}
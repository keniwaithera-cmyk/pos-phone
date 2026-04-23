package com.example.possystem.ui.theme.screens.product

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.possystem.data.ProductViewModel
import com.example.possystem.models.ProductModel
import com.example.possystem.navigation.ROUTE_VIEW_PRODUCT
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

@Composable
fun UpdateProductScreen(
    navController: NavController,
    productId: String,
    productViewModel: ProductViewModel = viewModel()
) {
    val context = LocalContext.current

    // ── Load product from Firebase ──────────────────────────────────────────
    var product by remember { mutableStateOf<ProductModel?>(null) }

    LaunchedEffect(productId) {
        val snapshot = FirebaseDatabase.getInstance()
            .getReference("Products")
            .child(productId)
            .get()
            .await()
        product = snapshot.getValue(ProductModel::class.java)?.apply { id = productId }
    }

    // Show spinner while loading
    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF880E4F))
        }
        return
    }

    // ── Editable field states (pre-filled from loaded product) ─────────────
    var productName     by remember { mutableStateOf(product!!.product_name    ?: "") }
    var price           by remember { mutableStateOf(product!!.price           ?: "") }
    var quantity        by remember { mutableStateOf(product!!.quantity        ?: "") }
    var description     by remember { mutableStateOf(product!!.description     ?: "") }
    var dateManufacture by remember { mutableStateOf(product!!.dateManufacture ?: "") }
    var barcodeNumber   by remember { mutableStateOf(product!!.barcodeNumber   ?: "") }
    var imageUri        by remember { mutableStateOf<Uri?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    // ── Navigate to Product List after successful update ────────────────────
    LaunchedEffect(productViewModel.navigateToViewProduct) {
        if (productViewModel.navigateToViewProduct) {
            productViewModel.onNavigationHandled()
            navController.navigate(ROUTE_VIEW_PRODUCT) {
                // Clear the update screen from the back stack so back button
                // doesn't return to it
                popUpTo(ROUTE_VIEW_PRODUCT) { inclusive = false }
            }
        }
    }

    // ── UI ──────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFCE4EC), Color(0xFFF8BBD0))
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            )  {

                Text(
                    text = "Update Product",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF880E4F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Image picker ────────────────────────────────────────────
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier
                        .size(140.dp)
                        .clickable { imageLauncher.launch("image/*") }
                        .shadow(8.dp, CircleShape)
                ) {
                    AnimatedContent(
                        targetState = imageUri,
                        label = "Image Picker Animation"
                    ) { targetUri ->
                        AsyncImage(
                            model = targetUri ?: product!!.imageUrl,
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Text(
                    text = "Tap to change picture",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 20.dp),
                    color = Color.LightGray,
                    thickness = 1.dp
                )

                // ── Form fields ─────────────────────────────────────────────
                val fieldModifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                val fieldShape = RoundedCornerShape(14.dp)

                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Product Name") },
                    placeholder = { Text("e.g., Laptop") },
                    modifier = fieldModifier,
                    shape = fieldShape,
                    singleLine = true
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price (KES)") },
                    placeholder = { Text("e.g., 1500") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = fieldModifier,
                    shape = fieldShape,
                    singleLine = true
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity in Stock") },
                    placeholder = { Text("e.g., 10") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = fieldModifier,
                    shape = fieldShape,
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("Brief product description") },
                    modifier = fieldModifier.height(120.dp),
                    shape = fieldShape,
                    maxLines = 5
                )

                OutlinedTextField(
                    value = dateManufacture,
                    onValueChange = { dateManufacture = it },
                    label = { Text("Date Manufactured") },
                    placeholder = { Text("e.g., 2024-01-15") },
                    modifier = fieldModifier,
                    shape = fieldShape,
                    singleLine = true
                )

                OutlinedTextField(
                    value = barcodeNumber,
                    onValueChange = { barcodeNumber = it },
                    label = { Text("Barcode Number") },
                    placeholder = { Text("e.g., 1234567890") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = fieldModifier,
                    shape = fieldShape,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Action buttons ──────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            navController.navigate(ROUTE_VIEW_PRODUCT) {
                                popUpTo(ROUTE_VIEW_PRODUCT) { inclusive = false }
                            }
                         },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.width(140.dp)
                    ) {
                        Text("Go Back", color = Color.DarkGray)
                    }

                    Button(
                        onClick = {
                            productViewModel.updateProduct(
                                productId        = productId,
                                imageUri         = imageUri,
                                product_name     = productName,
                                price            = price,
                                quantity         = quantity,
                                description      = description,
                                dateManufacture  = dateManufacture,
                                barcodeNumber    = barcodeNumber,
                                existingImageUrl = product!!.imageUrl,
                                context          = context
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD81B60)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.width(140.dp),
                        enabled = !productViewModel.isLoading
                    ) {
                        if (productViewModel.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Update", color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
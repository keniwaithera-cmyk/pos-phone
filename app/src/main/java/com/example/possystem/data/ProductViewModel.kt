package com.example.possystem.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.possystem.navigation.ROUTE_DASHBOARD
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream

class ProductViewModel : ViewModel() {
    val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dj3uizrt3/image/upload"
    val uploadPreset = "images_folder"

    fun uploadProduct(
        imageUri: Uri?,
        productName: String,
        price: String,
        quantity: String,
        description: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) }
                val ref = FirebaseDatabase.getInstance().getReference("product").push()
                val productData = mapOf(
                    "id" to ref.key,
                    "productName" to productName,
                    "price" to price,               // Fixed: matches ProductModel field name
                    "quantity" to quantity,          // Fixed: matches ProductModel field name
                    "description" to description,    // Fixed: matches ProductModel field name
                    "imageUrl" to imageUrl
                )
                ref.setValue(productData).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product saved successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_DASHBOARD)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product not saved: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun uploadToCloudinary(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes() ?: throw Exception("Image read failed")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), fileBytes)
            )
            .addFormDataPart("upload_preset", uploadPreset)
            .build()
        val request = Request.Builder().url(cloudinaryUrl).post(requestBody).build()
        val response = OkHttpClient().newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Upload failed: ${response.code}")
        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody ?: "")?.groupValues?.get(1)
        return secureUrl ?: throw Exception("Failed to get image URL")
    }
}
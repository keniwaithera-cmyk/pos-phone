package com.example.possystem.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.possystem.models.ProductModel
import com.example.possystem.navigation.ROUTE_DASHBOARD
import com.example.possystem.navigation.ROUTE_VIEW_PRODUCT
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

    private val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dd5f3uije/image/upload"
    private val uploadPreset = "images_folder"

    private val _products = mutableStateListOf<ProductModel>()
    val products: List<ProductModel> = _products

    // ─── Observable UI State ─────────────────────────────────────────────────

    var isLoading by mutableStateOf(false)
        private set

    var navigateToViewProduct by mutableStateOf(false)
        private set

    fun onNavigationHandled() {
        navigateToViewProduct = false
    }

    // ─── Upload / Add Product ────────────────────────────────────────────────

    fun uploadProduct(
        imageUri: Uri?,
        product_name: String,
        price: String,
        quantity: String,
        dateManufacture: String,
        barcodeNumber: String,
        description: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { isLoading = true }
            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) }
                val ref = FirebaseDatabase.getInstance().getReference("Products").push()
                val productData = mapOf(
                    "id" to ref.key,
                    "product_name" to product_name,
                    "price" to price,
                    "quantity" to quantity,
                    "dateManufacture" to dateManufacture,
                    "barcodeNumber" to barcodeNumber,
                    "description" to description,
                    "imageUrl" to imageUrl
                )
                ref.setValue(productData).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product saved successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_VIEW_PRODUCT)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to save product: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } finally {
                withContext(Dispatchers.Main) { isLoading = false }
            }
        }
    }

    // ─── Fetch Products ──────────────────────────────────────────────────────

    fun fetchProduct(context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Products")
        ref.get().addOnSuccessListener { snapshot ->
            _products.clear()
            for (child in snapshot.children) {
                val product = child.getValue(ProductModel::class.java)
                product?.let {
                    it.id = child.key
                    _products.add(it)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to load products", Toast.LENGTH_LONG).show()
        }
    }

    // ─── Delete Product ──────────────────────────────────────────────────────

    fun deleteProduct(productId: String, context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Products/$productId")
        ref.removeValue()
            .addOnSuccessListener {
                _products.removeAll { it.id == productId }
                Toast.makeText(context, "Product deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to delete product", Toast.LENGTH_SHORT).show()
            }
    }

    // ─── Update Product ──────────────────────────────────────────────────────

    fun updateProduct(
        productId: String,
        imageUri: Uri?,
        product_name: String,
        price: String,
        quantity: String,
        dateManufacture: String,
        barcodeNumber: String,
        description: String,
        existingImageUrl: String?,
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { isLoading = true }
            try {
                val imageUrl = if (imageUri != null) uploadToCloudinary(context, imageUri)
                else existingImageUrl

                val ref = FirebaseDatabase.getInstance().getReference("Products/$productId")
                val updates = mapOf(
                    "product_name" to product_name,
                    "price" to price,
                    "quantity" to quantity,
                    "dateManufacture" to dateManufacture,
                    "barcodeNumber" to barcodeNumber,
                    "description" to description,
                    "imageUrl" to imageUrl
                )
                ref.updateChildren(updates).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Product updated successfully", Toast.LENGTH_LONG).show()
                    navigateToViewProduct = true
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to update: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } finally {
                withContext(Dispatchers.Main) { isLoading = false }
            }
        }
    }


    fun getProductById(productId: String): ProductModel? {
        return _products.find { it.id == productId }
    }

    fun fetchSingleProduct(productId: String, onResult: (ProductModel?) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("Products/$productId")
        ref.get().addOnSuccessListener { snapshot ->
            val product = snapshot.getValue(ProductModel::class.java)
            product?.id = snapshot.key
            onResult(product)
        }.addOnFailureListener {
            onResult(null)
        }
    }

    // ─── Cloudinary Upload ───────────────────────────────────────────────────

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
        if (!response.isSuccessful) throw Exception("Upload failed")
        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody ?: "")?.groupValues?.get(1)
        return secureUrl ?: throw Exception("Failed to get image URL")
    }
}
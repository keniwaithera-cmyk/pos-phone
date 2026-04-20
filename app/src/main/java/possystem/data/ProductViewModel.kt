package com.example.possystem.data

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.possystem.models.ProductModel
import com.example.possystem.navigation.ROUTE_DASHBOARD

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
// FIX #4: Removed wrong "import okhttp3.OkHttp" — that class does not exist.
//         OkHttpClient below is the correct one.
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream

class ProductViewModel:ViewModel() {
    val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dd5f3uije/image/upload"
    val uploadPreset = "images_folder"

    fun uploadProduct(imageUri: Uri?,product_name:String,price:String,quantity
    :String, dateManufacture:String,barcodeNumber:String,description:String,
                      context: Context,navController: NavController){

        viewModelScope.launch (Dispatchers.IO){
            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context,it) }
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
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"Product saved Successfully",Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_DASHBOARD)
                }

            }catch (e: Exception){
                Log.e("ProductViewModel", "Error saving product", e)
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"Error: ${e.message}",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun uploadToCloudinary(context:Context,uri: Uri):String{
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes() ?: throw  Exception("Image read failed")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file","image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(),fileBytes))
            .addFormDataPart("upload_preset",uploadPreset).build()
        val request = Request.Builder().url(cloudinaryUrl).post(requestBody).build()
        val response = OkHttpClient().newCall(request).execute()
        if(!response.isSuccessful) throw Exception("Upload failed")
        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody ?: "")?.groupValues?.get(1)
        return secureUrl ?: throw Exception("Failed to get image URL")
    }

    private val _products = mutableStateListOf<ProductModel>()
    val products: List<ProductModel> = _products

    fun fetchProduct(context: Context){
        val ref = FirebaseDatabase.getInstance().getReference("Products")
        ref.get().addOnSuccessListener { snapshot ->
            _products.clear()
            for (child in snapshot.children){
                val product = child.getValue(ProductModel::class.java)
                product?.let { it.id = child.key
                    _products.add(it)}
            }
        }.addOnFailureListener {
            Toast.makeText(context,"Failed to load products", Toast.LENGTH_LONG).show()
        }
    }
}
package com.example.possystem.models

data class ProductModel(
    var id: String? = null,
    var productName: String? = null,
    var price: String? = null,
    var quantity: String? = null,
    var description: String? = null,
    var imageUrl: String? = null,
    var dateManufactured: String? = null,   // NEW: Date of manufacture
    var barcodeNumber: String? = null       // NEW: Barcode number (scanned or manual)
)
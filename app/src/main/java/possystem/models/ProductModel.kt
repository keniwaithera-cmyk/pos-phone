package com.example.possystem.models

data class ProductModel(
    var id:String?=null,
    var product_name:String?=null,
    var price: String? = null,
    var quantity: String? = null,
    var dateManufacture: String? =null,
    var barcodeNumber: String? =null,
    var description: String? =null,


    var imageUrl: String? = null,
)
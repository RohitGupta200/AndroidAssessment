package com.example.producthandling

import Product


interface StreamLibAPI {
    suspend fun getAllProducts(): Collection<Product>
    suspend fun getPriceTax(): Double
    suspend fun getCompanyUpdatedPrices(): ArrayList<Pair<Int, Double>>
    suspend fun getCompanyUpdatedStocks(): ArrayList<Pair<Int, Int>>
    suspend fun getProductsToDelete(): List<Int>
    suspend fun getNewProducts(): ArrayList<Product>
}

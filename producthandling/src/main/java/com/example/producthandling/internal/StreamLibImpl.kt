package com.example.producthandling.internal

import Product
import com.example.producthandling.StreamLibAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.coroutineContext

internal class StreamLibImpl : StreamLibAPI {
    internal val productList = ArrayList<Product>()
    private var initialized = false
    private val initMutex = Mutex()
    private val stockMutex = Mutex()
    private val descriptionMutex = Mutex()
    private val deleteMutex = Mutex()
    private val priceMutex = Mutex()

    override suspend fun getAllProducts(): Collection<Product> {
        initialized = false
        initMutex.withLock {
            if (!initialized) {
                (1..500_000).forEach {
                    val id = it
                    productList.add(
                        Product(
                            id = id,
                            name = "Product $it",
                            description = "Description $it",
                            price = (10..1000).random().toDouble(),
                            stock = (0..500).random()
                        )
                    )
                }
                initialized = true
            }
        }
        return productList
    }

    private suspend fun ensureInitialized() {
        initMutex.withLock {

            if (!initialized) delay(1)
        }
    }

    fun getRandomDistinctNumbers(count: Int, rangeStart: Int, rangeEnd: Int): List<Int> {
        val range = (rangeStart..rangeEnd)
        require(count <= range.count()) { "Count cannot be more than the number of elements in the range." }

        return range.shuffled().take(count)
    }


    override suspend fun getPriceTax(): Double {
        ensureInitialized()
        delay(1050)

        return (1..30).random().toDouble()
    }

    override suspend fun getCompanyUpdatedPrices(): ArrayList<Pair<Int, Double>> {
        ensureInitialized()
        delay(1000)
        val ids = getRandomDistinctNumbers((100000..productList.size).random(), 1, productList.size)
        val updatedProducts = ArrayList<Pair<Int, Double>>()
        ids.forEach {
            var updatedPrice = Pair(it, (100..999).random().toDouble())
            updatedProducts.add(updatedPrice)
        }
        return updatedProducts

    }

    override suspend fun getCompanyUpdatedStocks(): ArrayList<Pair<Int, Int>> {
        ensureInitialized()
        delay(1000)
        val ids = getRandomDistinctNumbers((100000..productList.size).random(), 1, productList.size)
        val updatedProducts = ArrayList<Pair<Int, Int>>()
        ids.forEach {
            var updatedPrice = Pair(it, (1..100).random())
            updatedProducts.add(updatedPrice)
        }
        return updatedProducts

    }

    override suspend fun getProductsToDelete(): List<Int> {
        ensureInitialized()
        delay(1000)
        return getRandomDistinctNumbers((100000..productList.size).random(), 1, productList.size)
    }

    override suspend fun getNewProducts(): ArrayList<Product> {
        ensureInitialized()
        delay(1000)

        val newProducts = ArrayList<Product>()
        (productList.size + 1..productList.size + 100000).forEach {

            val id = it
            newProducts.add(
                Product(
                    id = id,
                    name = "Product $it",
                    description = "Description $it",
                    price = (10..1000).random().toDouble(),
                    stock = (0..500).random()
                )
            )
        }
        CoroutineScope(coroutineContext).launch {
            addToOriginalList(newProducts)
        }
        return newProducts
    }
    private suspend fun ensureAllMutexes() {
        stockMutex.withLock {}
        descriptionMutex.withLock {}
        deleteMutex.withLock {}
        priceMutex.withLock {}
    }
    private suspend fun addToOriginalList(newProducts: ArrayList<Product>) {
        ensureAllMutexes()
        productList.addAll(newProducts)
    }
}

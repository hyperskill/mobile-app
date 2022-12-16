package org.hyperskill.app.products.domain.repository

import org.hyperskill.app.products.domain.model.Product

interface ProductsRepository {
    suspend fun getStreakFreezeProduct(): Result<Product>
    suspend fun buyProduct(productId: Long, count: Int): Result<Product>
}
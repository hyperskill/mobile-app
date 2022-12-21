package org.hyperskill.app.products.domain.interactor

import org.hyperskill.app.items.domain.model.Item
import org.hyperskill.app.products.domain.model.Product
import org.hyperskill.app.products.domain.repository.ProductsRepository

class ProductsInteractor(
    private val productsRepository: ProductsRepository
) {
    suspend fun getStreakFreezeProduct(): Result<Product> =
        productsRepository.getStreakFreezeProduct()

    suspend fun buyStreakFreeze(streakFreezeProductId: Long): Result<Item> =
        productsRepository.buyProduct(streakFreezeProductId, 1)
}
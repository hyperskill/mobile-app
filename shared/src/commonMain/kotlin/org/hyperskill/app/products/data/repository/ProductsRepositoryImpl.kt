package org.hyperskill.app.products.data.repository

import org.hyperskill.app.items.domain.model.Item
import org.hyperskill.app.products.data.source.ProductsRemoteDataSource
import org.hyperskill.app.products.domain.model.Product
import org.hyperskill.app.products.domain.repository.ProductsRepository
import org.hyperskill.app.products.remote.model.GetProductsRequest

class ProductsRepositoryImpl(
    private val productsRemoteDataSource: ProductsRemoteDataSource
) : ProductsRepository {
    override suspend fun getStreakFreezeProduct(): Result<Product> =
        kotlin.runCatching {
            productsRemoteDataSource.getProducts(
                GetProductsRequest(category = GetProductsRequest.Category.STREAK)
            ).getOrThrow().first()
        }

    override suspend fun buyProduct(productId: Long, count: Int): Result<Item> =
        productsRemoteDataSource.buyProduct(productId, count)
}
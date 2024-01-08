package org.hyperskill.app.products.data.repository

import org.hyperskill.app.items.domain.model.Item
import org.hyperskill.app.products.data.source.ProductsRemoteDataSource
import org.hyperskill.app.products.domain.model.Product
import org.hyperskill.app.products.domain.repository.ProductsRepository
import org.hyperskill.app.products.remote.model.GetProductsRequest

internal class ProductsRepositoryImpl(
    private val productsRemoteDataSource: ProductsRemoteDataSource
) : ProductsRepository {
    override suspend fun getStreakFreezeProduct(): Result<Product> =
        kotlin.runCatching {
            productsRemoteDataSource.getProducts(
                GetProductsRequest(type = GetProductsRequest.ProductType.STREAK_FREEZE)
            ).getOrThrow().first()
        }

    override suspend fun buyProduct(productId: Long, count: Int): Result<Item> =
        productsRemoteDataSource.buyProduct(productId, count)
}
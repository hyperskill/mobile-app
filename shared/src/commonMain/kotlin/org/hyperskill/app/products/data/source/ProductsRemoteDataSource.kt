package org.hyperskill.app.products.data.source

import org.hyperskill.app.products.domain.model.Product
import org.hyperskill.app.products.remote.model.GetProductsRequest

interface ProductsRemoteDataSource {
    suspend fun getProducts(request: GetProductsRequest): Result<List<Product>>
    suspend fun buyProduct(productId: Long, count: Int): Result<Product>
}
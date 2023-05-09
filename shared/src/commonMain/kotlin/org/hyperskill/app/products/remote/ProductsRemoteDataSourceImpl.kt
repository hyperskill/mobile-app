package org.hyperskill.app.products.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.items.domain.model.Item
import org.hyperskill.app.items.remote.model.ItemsResponse
import org.hyperskill.app.products.data.source.ProductsRemoteDataSource
import org.hyperskill.app.products.domain.model.Product
import org.hyperskill.app.products.remote.model.BuyProductRequest
import org.hyperskill.app.products.remote.model.GetProductsRequest
import org.hyperskill.app.products.remote.model.ProductsResponse

class ProductsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : ProductsRemoteDataSource {
    override suspend fun getProducts(request: GetProductsRequest): Result<List<Product>> =
        kotlin.runCatching {
            httpClient
                .get("/api/products") {
                    contentType(ContentType.Application.Json)
                    request.parameters.forEach { parameter(it.key, it.value.toString()) }
                }.body<ProductsResponse>().products
        }

    override suspend fun buyProduct(productId: Long, count: Int): Result<Item> =
        kotlin.runCatching {
            httpClient
                .post("/api/products/buy") {
                    contentType(ContentType.Application.Json)
                    setBody(listOf(BuyProductRequest(productId, count)))
                }.body<ItemsResponse>().items.first()
        }
}
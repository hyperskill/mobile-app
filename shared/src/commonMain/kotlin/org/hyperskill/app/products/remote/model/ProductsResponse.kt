package org.hyperskill.app.products.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.products.domain.model.Product

@Serializable
class ProductsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("products")
    val products: List<Product>
) : MetaResponse
package org.hyperskill.app.products.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuyProductRequest(
    @SerialName("product")
    val productId: Long,
    @SerialName("count")
    val count: Int,
)
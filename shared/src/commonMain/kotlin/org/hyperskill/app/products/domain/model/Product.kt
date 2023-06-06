package org.hyperskill.app.products.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("id")
    val id: Long,
    @SerialName("price")
    val price: Int
)
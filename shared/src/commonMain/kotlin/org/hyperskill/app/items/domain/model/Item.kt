package org.hyperskill.app.items.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("id")
    val id: Long,
    @SerialName("product_id")
    val productId: Long,
    @SerialName("used_at")
    val usedAt: Instant?
)
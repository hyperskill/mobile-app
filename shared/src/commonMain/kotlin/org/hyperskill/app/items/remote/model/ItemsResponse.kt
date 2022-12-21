package org.hyperskill.app.items.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.items.domain.model.Item

@Serializable
class ItemsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("items")
    val items: List<Item>
) : MetaResponse
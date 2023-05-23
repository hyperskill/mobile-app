package org.hyperskill.app.magic_links.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MagicLinksRequest(
    @SerialName("next_url")
    val nextUrl: String
)
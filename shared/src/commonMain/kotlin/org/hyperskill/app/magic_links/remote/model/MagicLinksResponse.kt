package org.hyperskill.app.magic_links.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.magic_links.domain.model.MagicLink

@Serializable
class MagicLinksResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("magic-links")
    val magicLinks: List<MagicLink>
) : MetaResponse
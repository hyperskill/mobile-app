package org.hyperskill.app.badges.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.badges.domain.model.Badge
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse

@Serializable
class BadgesResponse(
    @SerialName("meta")
    override val meta: Meta,
    @SerialName("badges")
    val badges: List<Badge>
) : MetaResponse
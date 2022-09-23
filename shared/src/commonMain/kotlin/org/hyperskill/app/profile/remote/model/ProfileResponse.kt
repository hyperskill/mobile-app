package org.hyperskill.app.profile.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.profile.domain.model.Profile

@Serializable
class ProfileResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("profiles")
    val profiles: List<Profile>
) : MetaResponse
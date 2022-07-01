package org.hyperskill.app.track.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.track.domain.model.Track

@Serializable
class TrackResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("tracks")
    val tracks: List<Track>
) : MetaResponse

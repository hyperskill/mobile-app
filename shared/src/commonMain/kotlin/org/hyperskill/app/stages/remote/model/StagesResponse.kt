package org.hyperskill.app.stages.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.stages.domain.model.Stage

@Serializable
class StagesResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("stages")
    val stages: List<Stage>
) : MetaResponse
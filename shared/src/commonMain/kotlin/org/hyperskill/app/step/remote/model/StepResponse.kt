package org.hyperskill.app.step.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.step.domain.model.Step

@Serializable
class StepResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("steps")
    val steps: List<Step>
) : MetaResponse
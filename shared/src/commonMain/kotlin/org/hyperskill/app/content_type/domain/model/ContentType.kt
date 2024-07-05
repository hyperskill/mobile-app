package org.hyperskill.app.content_type.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ContentType {
    @SerialName("step")
    STEP,
    @SerialName("stage")
    STAGE,

    UNKNOWN
}
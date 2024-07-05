package org.hyperskill.app.content_type.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ContentType {
    @SerialName("step")
    STEP,
    @SerialName("dailystep")
    DAILY_STEP,
    @SerialName("stage")
    STAGE,
    @SerialName("project")
    PROJECT,
    @SerialName("topic")
    TOPIC,

    UNKNOWN
}
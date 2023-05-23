package org.hyperskill.app.projects.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProjectKind {
    @SerialName("graduate")
    GRADUATE,
    @SerialName("auto")
    AUTO
}
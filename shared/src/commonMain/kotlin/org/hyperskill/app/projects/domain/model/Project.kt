package org.hyperskill.app.projects.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Project(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("is_deprecated")
    val isDeprecated: Boolean = false,
    @SerialName("progress_id")
    val progressId: String,
    @Transient
    val progress: ProjectProgress? = null
)

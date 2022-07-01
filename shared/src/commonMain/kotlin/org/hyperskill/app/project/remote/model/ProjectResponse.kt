package org.hyperskill.app.project.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.project.domain.model.Project

@Serializable
class ProjectResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("projects")
    val projects: List<Project>
) : MetaResponse
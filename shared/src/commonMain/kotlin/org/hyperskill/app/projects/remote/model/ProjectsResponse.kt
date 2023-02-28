package org.hyperskill.app.projects.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.projects.domain.model.Project

@Serializable
class ProjectsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("projects")
    val projects: List<Project>
) : MetaResponse
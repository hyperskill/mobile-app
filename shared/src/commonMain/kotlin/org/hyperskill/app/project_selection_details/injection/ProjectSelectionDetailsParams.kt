package org.hyperskill.app.project_selection_details.injection

import kotlinx.serialization.Serializable

@Serializable
data class ProjectSelectionDetailsParams(
    val trackId: Long,
    val projectId: Long,
    val isProjectSelected: Boolean
)
package org.hyperskill.app.project_selection.details.injection

import kotlinx.serialization.Serializable

@Serializable
data class ProjectSelectionDetailsParams(
    val isNewUserMode: Boolean,
    val trackId: Long,
    val projectId: Long,
    val isProjectSelected: Boolean,
    val isProjectBestRated: Boolean,
    val isProjectFastestToComplete: Boolean
)
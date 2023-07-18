package org.hyperskill.app.project_selection.list.injection

import kotlinx.serialization.Serializable

@Serializable
data class ProjectSelectionListParams(
    val trackId: Long,
    val isNewUserMode: Boolean = false
)
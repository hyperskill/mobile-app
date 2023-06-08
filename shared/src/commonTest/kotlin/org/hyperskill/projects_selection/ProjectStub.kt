package org.hyperskill.projects_selection

import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectTracksEntry

fun Project.Companion.stub(
    id: Long,
    tracks: Map<String, ProjectTracksEntry> = emptyMap(),
    defaultScore: Float = 0f
): Project =
    Project(
        id = id,
        title = "",
        progressId = "",
        tracks = tracks,
        isIdeRequired = false,
        defaultScore = defaultScore
    )
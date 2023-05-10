package org.hyperskill.project

import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectTracksEntry

fun Project.Companion.stub(
    id: Long,
    tracks: Map<String, ProjectTracksEntry> = emptyMap()
): Project =
    Project(
        id = id,
        title= "",
        isDeprecated = false,
        progressId= "",
        tracks = tracks,
        isIdeRequired = false
    )
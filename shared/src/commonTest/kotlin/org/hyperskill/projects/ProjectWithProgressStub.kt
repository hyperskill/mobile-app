package org.hyperskill.projects

import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectWithProgress

fun ProjectWithProgress.Companion.stub(
    projectId: Long = 0L
): ProjectWithProgress =
    ProjectWithProgress(
        project = Project.stub(projectId),
        progress = ProjectProgress.stub(projectId)
    )
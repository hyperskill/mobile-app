package org.hyperskill.projects_selection

import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectWithProgress

fun ProjectWithProgress.Companion.stub(
    projectId: Long = 0L,
    isCompleted: Boolean = false,
    completedStages: List<Long> = emptyList(),
    stagesIds: List<Long> = listOf(0)
): ProjectWithProgress =
    ProjectWithProgress(
        project = Project.stub(
            id = projectId,
            stagesIds = stagesIds
        ),
        progress = ProjectProgress.stub(
            projectId = projectId,
            isCompleted = isCompleted,
            completedStages = completedStages
        )
    )
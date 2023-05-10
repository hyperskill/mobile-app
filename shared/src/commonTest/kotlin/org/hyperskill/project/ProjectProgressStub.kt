package org.hyperskill.project

import org.hyperskill.app.projects.domain.model.ProjectProgress

fun ProjectProgress.Companion.stub(projectId: Long): ProjectProgress =
    ProjectProgress(
        id = "",
        vid = "project-$projectId",
        isCompleted = false,
        clarity = null,
        funMeasure = null,
        usefulness = null,
        secondsToComplete = .0
    )
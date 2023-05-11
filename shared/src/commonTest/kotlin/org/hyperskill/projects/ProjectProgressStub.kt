package org.hyperskill.projects

import org.hyperskill.app.projects.domain.model.ProjectProgress

fun ProjectProgress.Companion.stub(
    projectId: Long,
    clarity: Float? = null,
    funMeasure: Float? = null,
    usefulness: Float? = null,
    secondsToComplete: Double = .0
): ProjectProgress =
    ProjectProgress(
        id = "",
        vid = "project-$projectId",
        isCompleted = false,
        clarity = clarity,
        funMeasure = funMeasure,
        usefulness = usefulness,
        secondsToComplete = secondsToComplete
    )
package org.hyperskill.projects_selection

import org.hyperskill.app.projects.domain.model.ProjectProgress

fun ProjectProgress.Companion.stub(
    projectId: Long,
    clarity: Float? = null,
    funMeasure: Float? = null,
    usefulness: Float? = null,
    secondsToComplete: Float = 0f,
    baseScore: Float = 0f,
    featureScore: Float = 0f
): ProjectProgress =
    ProjectProgress(
        id = "",
        vid = "project-$projectId",
        isCompleted = false,
        clarity = clarity,
        funMeasure = funMeasure,
        usefulness = usefulness,
        secondsToComplete = secondsToComplete,
        featureScore = featureScore,
        baseScore = baseScore,
        completedStages = emptyList()
    )
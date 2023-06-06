package org.hyperskill.track

import org.hyperskill.app.track.domain.model.ProjectsByLevel
import org.hyperskill.app.track.domain.model.Track

fun Track.Companion.stub(
    id: Long,
    projects: List<Long> = emptyList(),
    projectsByLevel: ProjectsByLevel = ProjectsByLevel(),
    topicProviders: List<Long> = emptyList(),
    canIssueCertificate: Boolean = false
): Track =
    Track(
        id = id,
        description = "",
        isBeta = false,
        isCompleted = false,
        canIssueCertificate = canIssueCertificate,
        projectsByLevel = projectsByLevel,
        secondsToComplete = .0f,
        title = "",
        topicsCount = 0,
        cover = "",
        projects = projects,
        progressId = "",
        capstoneProjects = emptyList(),
        capstoneTopicsCount = 0,
        topicProviders = topicProviders,
        betaProjects = listOf(),
        progress = null,
        providerId = 0
    )
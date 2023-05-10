package org.hyperskill.track

import org.hyperskill.app.track.domain.model.ProjectsByLevel
import org.hyperskill.app.track.domain.model.Track

fun Track.Companion.stub(
    id: Long,
    projects: List<Long> = emptyList(),
    projectsByLevel: ProjectsByLevel = ProjectsByLevel()
): Track =
    Track(
        id = id,
        description= "",
        isBeta = false,
        isFree = false,
        isCompleted = false,
        canIssueCertificate = false,
        projectsByLevel = projectsByLevel,
        results = "",
        secondsToComplete = .0,
        title = "",
        topicsCount = 0,
        cover = "",
        careers = "",
        projects = projects,
        progressId = "",
        isPublic = false,
        capstoneProjects = emptyList(),
        capstoneTopicsCount = 0,
        topicProviders = emptyList(),
    )
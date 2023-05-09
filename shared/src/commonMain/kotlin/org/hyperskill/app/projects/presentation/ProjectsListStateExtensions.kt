package org.hyperskill.app.projects.presentation

import org.hyperskill.app.progresses.domain.model.averageRating
import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.track.domain.model.getProjectsIds

val ProjectsListFeature.ContentState.Content.selectedProject: ProjectWithProgress?
    get() = selectedProjectId?.let { projects[it] }

val ProjectsListFeature.ContentState.Content.recommendedProjects: List<ProjectWithProgress>
    get() = recommendedProjectsIds.mapNotNull { projectsId ->
        projects[projectsId]
    }

val ProjectsListFeature.ContentState.Content.projectsByLevel: Map<ProjectLevel, List<ProjectWithProgress>>
    get() = ProjectLevel.values().associateWith { level ->
        val projectsIds = track.projectsByLevel.getProjectsIds(level)
        if (!projectsIds.isNullOrEmpty()) {
            val filteredProjectIds = if (selectedProjectId != null) {
                projectsIds - selectedProjectId
            } else {
                projectsIds
            }
            filteredProjectIds.toSet().mapNotNull { projectId ->
                projects[projectId]
            }
        } else {
            emptyList()
        }
    }

private val ProjectsListFeature.ContentState.Content.recommendedProjectsIds: List<Long>
    get() = track.projects.take(6)

val ProjectsListFeature.ContentState.Content.bestRatedProjectId: Long?
    get() = projects.values.reduceOrNull { localBest, current ->
        if (localBest.progress.averageRating() > current.progress.averageRating()) {
            localBest
        } else {
            current
        }
    }?.project?.id
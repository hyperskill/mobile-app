package org.hyperskill.app.projects.presentation

import org.hyperskill.app.progresses.domain.model.averageRating
import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.track.domain.model.getProjectsIds

internal val ProjectsListFeature.ContentState.Content.selectedProject: ProjectWithProgress?
    get() = selectedProjectId?.let { projects[it] }

internal val ProjectsListFeature.ContentState.Content.recommendedProjects: List<ProjectWithProgress>
    get() = excludeSelectedProject(track.projects, selectedProjectId).take(6).mapNotNull { projectsId ->
        projects[projectsId]
    }

internal val ProjectsListFeature.ContentState.Content.projectsByLevel: Map<ProjectLevel, List<ProjectWithProgress>>
    get() = ProjectLevel.values().associateWith { level ->
        val projectsIds = track.projectsByLevel.getProjectsIds(level)
        if (!projectsIds.isNullOrEmpty()) {
            excludeSelectedProject(projectsIds, selectedProjectId).mapNotNull { projectId ->
                projects[projectId]
            }
        } else {
            emptyList()
        }
    }

private fun excludeSelectedProject(projectsIds: List<Long>, selectedProjectId: Long?): List<Long> =
    if (selectedProjectId != null) {
        projectsIds - selectedProjectId
    } else {
        projectsIds
    }

internal val ProjectsListFeature.ContentState.Content.bestRatedProjectId: Long?
    get() {
        val bestRatedProject = projects.values.reduceOrNull { localBest, current ->
            if (localBest.progress.averageRating() > current.progress.averageRating()) {
                localBest
            } else {
                current
            }
        }
        val bestRatedProjectRating = bestRatedProject?.progress?.averageRating()
        return if (bestRatedProjectRating != null && bestRatedProjectRating > 0) {
            bestRatedProject.project.id
        } else {
            null
        }
    }
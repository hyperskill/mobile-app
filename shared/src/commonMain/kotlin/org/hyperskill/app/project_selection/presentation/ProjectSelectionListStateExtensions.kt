package org.hyperskill.app.project_selection.presentation

import org.hyperskill.app.progresses.domain.model.averageRating
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.ContentState
import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.track.domain.model.getProjectsIds

internal val ContentState.Content.selectedProject: ProjectWithProgress?
    get() = currentProjectId?.let { projects[it] }

internal val ContentState.Content.recommendedProjects: List<ProjectWithProgress>
    get() =
        excludeSelectedProject(track.projects, currentProjectId)
            .take(ProjectSelectionListFeature.BEST_RATED_PROJECTS_COUNT)
            .mapNotNull { projectsId -> projects[projectsId] }

internal val ContentState.Content.projectsByLevel: Map<ProjectLevel, List<ProjectWithProgress>>
    get() = ProjectLevel.values().associateWith { level ->
        val projectsIds = track.projectsByLevel.getProjectsIds(level)
        if (!projectsIds.isNullOrEmpty()) {
            excludeSelectedProject(projectsIds, currentProjectId).mapNotNull { projectId ->
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

internal val ContentState.Content.bestRatedProjectId: Long?
    get() {
        val bestRatedProject = projects.values.maxByOrNull {
            it.progress.averageRating()
        }
        val bestRatedProjectRating = bestRatedProject?.progress?.averageRating()
        return if (bestRatedProjectRating != null && bestRatedProjectRating > 0) {
            bestRatedProject.project.id
        } else {
            null
        }
    }

/**
 * @return project id with the shortest time-to-complete or null if there is no such project
 */
internal val ContentState.Content.fastestToCompleteProjectId: Long?
    get() {
        val fastestToCompleteProject = projects.values.minByOrNull {
            it.progress.secondsToComplete ?: 0f
        }
        val fastestTimeToComplete = fastestToCompleteProject?.progress?.secondsToComplete
        return if (fastestTimeToComplete != null && fastestTimeToComplete > .0) {
            fastestToCompleteProject.project.id
        } else {
            null
        }
    }
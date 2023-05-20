package org.hyperskill.app.project_selection.presentation

import org.hyperskill.app.progresses.domain.model.averageRating
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.ContentState
import org.hyperskill.app.projects.domain.model.ProjectLevel
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.track.domain.model.asLevelByProjectIdMap

internal val ContentState.Content.selectedProject: ProjectWithProgress?
    get() = currentProjectId?.let { projects[it] }

internal val ContentState.Content.recommendedProjects: List<ProjectWithProgress>
    get() = excludeSelectedProject(sortedProjectsIds, currentProjectId)
        .take(ProjectSelectionListFeature.BEST_RATED_PROJECTS_COUNT)
        .mapNotNull { projectsId -> projects[projectsId] }

internal val ContentState.Content.projectsByLevel: Map<ProjectLevel, List<ProjectWithProgress>>
    get() = buildMap<ProjectLevel, MutableList<ProjectWithProgress>> {
        val levelByProjectIdMap = track.projectsByLevel.asLevelByProjectIdMap()
        excludeSelectedProject(sortedProjectsIds, currentProjectId)
            .forEach { projectId ->
                val level = levelByProjectIdMap[projectId]
                if (level != null) {
                    val project = projects[projectId]
                    if (project != null) {
                        val projects = getOrPut(level) { mutableListOf() }
                        projects.add(project)
                    }
                }
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
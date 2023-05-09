package org.hyperskill.app.projects.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.model.projectId
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.repository.TrackRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProjectsListActionDispatcher(
    config: ActionDispatcherOptions,
    private val trackRepository: TrackRepository,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    private val projectsRepository: ProjectsRepository,
    private val progressesRepository: ProgressesRepository
) : CoroutineActionDispatcher<ProjectsListFeature.Action, ProjectsListFeature.Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: ProjectsListFeature.Action) {
        when (action) {
            is ProjectsListFeature.Action.FetchContent -> {
                coroutineScope {
                    val track =
                        trackRepository.getTrack(action.trackId, action.forceLoadFromNetwork)
                            .getOrElse {
                                onNewMessage(ProjectsListFeature.Message.ContentFetchResult.Error)
                                return@coroutineScope
                            }
                    val studyPlanDeferred = async {
                        currentStudyPlanStateRepository.getState(action.forceLoadFromNetwork)
                    }
                    val projectsDeferred = async {
                        projectsRepository.getProjects(track.projects)
                    }
                    val projectsProgressesDeferred = async {
                        progressesRepository.getProjectsProgresses(track.projects, action.forceLoadFromNetwork)
                    }
                    val studyPlan = studyPlanDeferred.await()
                        .getOrElse {
                            onNewMessage(ProjectsListFeature.Message.ContentFetchResult.Error)
                            return@coroutineScope
                        }
                    val projects = projectsDeferred.await()
                        .getOrElse {
                            onNewMessage(ProjectsListFeature.Message.ContentFetchResult.Error)
                            return@coroutineScope
                        }
                    val projectsProgresses: Map<Long?, ProjectProgress> =
                        projectsProgressesDeferred.await()
                            .map { progresses -> progresses.associateBy { it.projectId } }
                            .getOrElse {
                                onNewMessage(ProjectsListFeature.Message.ContentFetchResult.Error)
                                return@coroutineScope
                            }
                    onNewMessage(
                        ProjectsListFeature.Message.ContentFetchResult.Success(
                            track = track,
                            projects = projects.mapNotNull { project ->
                                ProjectWithProgress(
                                    project = project,
                                    progress = projectsProgresses[project.id] ?: return@mapNotNull null
                                )
                            },
                            selectedProjectId = studyPlan.projectId
                        )
                    )
                }
            }
        }
    }
}
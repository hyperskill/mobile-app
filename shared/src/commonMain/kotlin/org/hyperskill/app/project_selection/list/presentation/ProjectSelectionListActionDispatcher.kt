package org.hyperskill.app.project_selection.list.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.InternalAction
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.model.projectId
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.model.getAllProjects
import org.hyperskill.app.track.domain.repository.TrackRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

// TODO: extract all logic of this class to a dedicated interactor.
internal class ProjectSelectionListActionDispatcher(
    config: ActionDispatcherOptions,
    private val trackRepository: TrackRepository,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    private val projectsRepository: ProjectsRepository,
    private val progressesRepository: ProgressesRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<ProjectSelectionListFeature.Action, Message>(
    config.createConfig()
) {
    override suspend fun doSuspendableAction(action: ProjectSelectionListFeature.Action) {
        when (action) {
            is InternalAction.FetchContent ->
                handleFetchContentAction(action, ::onNewMessage)
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchContentAction(
        action: InternalAction.FetchContent,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildProjectSelectionListScreenRemoteDataLoading(),
            onError = { ProjectSelectionListFeature.ContentFetchResult.Error }
        ) {
            coroutineScope {
                val profile = currentProfileStateRepository.getState(forceUpdate = false).getOrThrow()
                val track = trackRepository.getTrack(action.trackId, action.forceLoadFromNetwork).getOrThrow()

                val projectsIds = track.getAllProjects(profile.isBeta)

                val studyPlanDeferred = async {
                    currentStudyPlanStateRepository.getState(action.forceLoadFromNetwork)
                }
                val projectsDeferred = async {
                    projectsRepository.getProjects(projectsIds, action.forceLoadFromNetwork)
                }
                val projectsProgressesDeferred = async {
                    progressesRepository.getProjectsProgresses(projectsIds, action.forceLoadFromNetwork)
                }

                val studyPlan = studyPlanDeferred.await().getOrThrow()
                val projects = projectsDeferred.await().getOrThrow()
                val projectsProgresses: Map<Long, ProjectProgress> =
                    projectsProgressesDeferred.await().map(::mapProgressesToMap).getOrThrow()

                ProjectSelectionListFeature.ContentFetchResult.Success(
                    profile = profile,
                    track = track,
                    projects = projects.mapNotNull { project ->
                        ProjectWithProgress(
                            project = project,
                            progress = projectsProgresses[project.id]
                                ?: return@mapNotNull null
                        )
                    },
                    currentProjectId = studyPlan.projectId
                )
            }
        }.let(onNewMessage)
    }

    private fun mapProgressesToMap(progresses: List<ProjectProgress>): Map<Long, ProjectProgress> =
        buildMap(progresses.size) {
            progresses.forEach { progress ->
                val projectId = progress.projectId
                if (projectId != null) {
                    put(projectId, progress)
                }
            }
        }
}
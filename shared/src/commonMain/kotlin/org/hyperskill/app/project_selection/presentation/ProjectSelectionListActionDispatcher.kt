package org.hyperskill.app.project_selection.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.InternalAction
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.model.projectId
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.repository.TrackRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProjectSelectionListActionDispatcher(
    config: ActionDispatcherOptions,
    private val trackRepository: TrackRepository,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    private val projectsRepository: ProjectsRepository,
    private val progressesRepository: ProgressesRepository,
    private val profileInteractor: ProfileInteractor,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<ProjectSelectionListFeature.Action, ProjectSelectionListFeature.Message>(
    config.createConfig()
) {
    override suspend fun doSuspendableAction(action: ProjectSelectionListFeature.Action) {
        when (action) {
            is InternalAction.FetchContent -> {
                coroutineScope {
                    val transaction = HyperskillSentryTransactionBuilder.buildProjectsListScreenRemoteDataLoading()
                    sentryInteractor.startTransaction(transaction)

                    val track =
                        trackRepository.getTrack(action.trackId, action.forceLoadFromNetwork)
                            .getOrElse {
                                sentryInteractor.finishTransaction(transaction, throwable = it)
                                onNewMessage(ProjectSelectionListFeature.ContentFetchResult.Error)
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
                            sentryInteractor.finishTransaction(transaction, throwable = it)
                            onNewMessage(ProjectSelectionListFeature.ContentFetchResult.Error)
                            return@coroutineScope
                        }
                    val projects = projectsDeferred.await()
                        .getOrElse {
                            sentryInteractor.finishTransaction(transaction, throwable = it)
                            onNewMessage(ProjectSelectionListFeature.ContentFetchResult.Error)
                            return@coroutineScope
                        }
                    val projectsProgresses: Map<Long?, ProjectProgress> =
                        projectsProgressesDeferred.await()
                            .map { progresses -> progresses.associateBy { it.projectId } }
                            .getOrElse {
                                sentryInteractor.finishTransaction(transaction, throwable = it)
                                onNewMessage(ProjectSelectionListFeature.ContentFetchResult.Error)
                                return@coroutineScope
                            }
                    onNewMessage(
                        ProjectSelectionListFeature.ContentFetchResult.Success(
                            track = track,
                            projects = projects.mapNotNull { project ->
                                ProjectWithProgress(
                                    project = project,
                                    progress = projectsProgresses[project.id]
                                        ?: return@mapNotNull null
                                )
                            },
                            selectedProjectId = studyPlan.projectId
                        )
                    )
                }
            }
            is InternalAction.SelectProject -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse {
                        return onNewMessage(ProjectSelectionListFeature.ProjectSelectionResult.Error)
                    }
                profileInteractor.selectTrackWithProject(
                    profileId = currentProfile.id,
                    trackId = action.trackId,
                    projectId = action.projectId
                ).getOrElse {
                    return onNewMessage(ProjectSelectionListFeature.ProjectSelectionResult.Error)
                }
                onNewMessage(ProjectSelectionListFeature.ProjectSelectionResult.Success)
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            else -> {
                // no op
            }
        }
    }
}
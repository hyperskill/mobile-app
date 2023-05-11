package org.hyperskill.app.projects.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.model.projectId
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.projects.presentation.ProjectsListFeature.InternalAction
import org.hyperskill.app.projects.presentation.ProjectsListFeature.InternalMessage
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.repository.TrackRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProjectsListActionDispatcher(
    config: ActionDispatcherOptions,
    private val trackRepository: TrackRepository,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    private val projectsRepository: ProjectsRepository,
    private val progressesRepository: ProgressesRepository,
    private val profileInteractor: ProfileInteractor,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<ProjectsListFeature.Action, ProjectsListFeature.Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: ProjectsListFeature.Action) {
        when (action) {
            is InternalAction.FetchContent -> {
                coroutineScope {
                    val transaction = HyperskillSentryTransactionBuilder.buildProjectsListScreenRemoteDataLoading()
                    sentryInteractor.startTransaction(transaction)

                    val track =
                        trackRepository.getTrack(action.trackId, action.forceLoadFromNetwork)
                            .getOrElse {
                                sentryInteractor.finishTransaction(transaction, throwable = it)
                                onNewMessage(InternalMessage.ContentFetchResult.Error)
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
                            onNewMessage(InternalMessage.ContentFetchResult.Error)
                            return@coroutineScope
                        }
                    val projects = projectsDeferred.await()
                        .getOrElse {
                            sentryInteractor.finishTransaction(transaction, throwable = it)
                            onNewMessage(InternalMessage.ContentFetchResult.Error)
                            return@coroutineScope
                        }
                    val projectsProgresses: Map<Long?, ProjectProgress> =
                        projectsProgressesDeferred.await()
                            .map { progresses -> progresses.associateBy { it.projectId } }
                            .getOrElse {
                                sentryInteractor.finishTransaction(transaction, throwable = it)
                                onNewMessage(InternalMessage.ContentFetchResult.Error)
                                return@coroutineScope
                            }
                    onNewMessage(
                        InternalMessage.ContentFetchResult.Success(
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
            is InternalAction.SelectProject -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse {
                        return onNewMessage(InternalMessage.ProjectSelectionResult.Error)
                    }
                profileInteractor.selectTrackWithProject(
                    profileId = currentProfile.id,
                    trackId = action.trackId,
                    projectId = action.projectId
                ).getOrElse {
                    return onNewMessage(InternalMessage.ProjectSelectionResult.Error)
                }
                onNewMessage(InternalMessage.ProjectSelectionResult.Success)
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
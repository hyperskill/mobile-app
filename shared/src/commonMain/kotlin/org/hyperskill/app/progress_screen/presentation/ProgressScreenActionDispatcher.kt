package org.hyperskill.app.progress_screen.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature.InternalAction
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature.Message
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.domain.model.TrackWithProgress
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class ProgressScreenActionDispatcher(
    config: ActionDispatcherOptions,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val trackInteractor: TrackInteractor,
    private val projectsRepository: ProjectsRepository,
    private val progressesInteractor: ProgressesInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<ProgressScreenFeature.Action, Message>(
    config.createConfig()
) {
    override suspend fun doSuspendableAction(action: ProgressScreenFeature.Action) {
        when (action) {
            is InternalAction.FetchTrackWithProgress -> {
                val transaction = HyperskillSentryTransactionBuilder
                    .buildProgressScreenRemoteTrackWithProgressLoading()
                sentryInteractor.startTransaction(transaction)

                val profile = currentProfileStateRepository
                    .getState(forceUpdate = action.forceLoadFromNetwork)
                    .getOrElse {
                        sentryInteractor.finishTransaction(transaction, throwable = it)
                        return onNewMessage(ProgressScreenFeature.TrackWithProgressFetchResult.Error)
                    }

                val studyPlan = currentStudyPlanStateRepository
                    .getState(forceUpdate = action.forceLoadFromNetwork)
                    .getOrElse {
                        sentryInteractor.finishTransaction(transaction, throwable = it)
                        return onNewMessage(ProgressScreenFeature.TrackWithProgressFetchResult.Error)
                    }

                if (studyPlan.trackId == null) {
                    sentryInteractor.finishTransaction(transaction)
                    return onNewMessage(ProgressScreenFeature.TrackWithProgressFetchResult.Error)
                }

                val trackWithProgress = fetchTrackWithProgress(studyPlan.trackId, action.forceLoadFromNetwork)
                    .getOrElse {
                        sentryInteractor.finishTransaction(transaction, throwable = it)
                        return onNewMessage(ProgressScreenFeature.TrackWithProgressFetchResult.Error)
                    }

                if (trackWithProgress == null) {
                    sentryInteractor.finishTransaction(transaction)
                    return onNewMessage(ProgressScreenFeature.TrackWithProgressFetchResult.Error)
                }

                sentryInteractor.finishTransaction(transaction)
                onNewMessage(
                    ProgressScreenFeature.TrackWithProgressFetchResult.Success(
                        trackWithProgress = trackWithProgress,
                        studyPlan = studyPlan,
                        profile = profile
                    )
                )
            }
            is InternalAction.FetchProjectWithProgress -> {
                val transaction = HyperskillSentryTransactionBuilder
                    .buildProgressScreenRemoteProjectWithProgressLoading()
                sentryInteractor.startTransaction(transaction)

                val studyPlan = currentStudyPlanStateRepository
                    .getState(forceUpdate = action.forceLoadFromNetwork)
                    .getOrElse {
                        sentryInteractor.finishTransaction(transaction, throwable = it)
                        return onNewMessage(ProgressScreenFeature.ProjectWithProgressFetchResult.Error)
                    }

                if (studyPlan.projectId == null) {
                    sentryInteractor.finishTransaction(transaction)
                    return onNewMessage(ProgressScreenFeature.ProjectWithProgressFetchResult.Empty)
                }

                val projectWithProgress = fetchProjectWithProgress(studyPlan.projectId, action.forceLoadFromNetwork)
                    .getOrElse {
                        sentryInteractor.finishTransaction(transaction, throwable = it)
                        return onNewMessage(ProgressScreenFeature.ProjectWithProgressFetchResult.Error)
                    }

                if (projectWithProgress == null) {
                    sentryInteractor.finishTransaction(transaction)
                    return onNewMessage(ProgressScreenFeature.ProjectWithProgressFetchResult.Empty)
                }

                sentryInteractor.finishTransaction(transaction)
                onNewMessage(
                    ProgressScreenFeature.ProjectWithProgressFetchResult.Success(
                        projectWithProgress = projectWithProgress,
                        studyPlan = studyPlan
                    )
                )
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
        }
    }

    private suspend fun fetchTrackWithProgress(
        trackId: Long,
        forceLoadFromRemote: Boolean
    ): Result<TrackWithProgress?> =
        coroutineScope {
            kotlin.runCatching {
                val trackDeferred = async {
                    trackInteractor.getTrack(trackId, forceLoadFromRemote)
                }
                val trackProgressDeferred = async {
                    progressesInteractor.getTrackProgress(trackId, forceLoadFromRemote)
                }
                TrackWithProgress(
                    track = trackDeferred.await().getOrThrow(),
                    trackProgress = trackProgressDeferred.await().getOrThrow() ?: return@runCatching null
                )
            }
        }

    private suspend fun fetchProjectWithProgress(
        projectId: Long,
        forceLoadFromRemote: Boolean
    ): Result<ProjectWithProgress?> =
        coroutineScope {
            kotlin.runCatching {
                val projectDeferred = async {
                    projectsRepository.getProject(projectId, forceLoadFromRemote)
                }
                val projectProgressDeferred = async {
                    progressesInteractor.getProjectProgress(projectId, forceLoadFromRemote)
                }
                ProjectWithProgress(
                    project = projectDeferred.await().getOrThrow(),
                    progress = projectProgressDeferred.await().getOrThrow()
                )
            }
        }
}
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
                handleFetchTrackWithProgressAction(action, ::onNewMessage)
            }
            is InternalAction.FetchProjectWithProgress -> {
                handleFetchProjectWithProgressAction(action, ::onNewMessage)
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchTrackWithProgressAction(
        action: InternalAction.FetchTrackWithProgress,
        onNewMessage: (Message) -> Unit
    ) {
        coroutineScope {
            val transaction = HyperskillSentryTransactionBuilder
                .buildProgressScreenRemoteTrackWithProgressLoading()
            sentryInteractor.startTransaction(transaction)

            val profileDeferred = async {
                currentProfileStateRepository
                    .getState(forceUpdate = action.forceLoadFromNetwork)
            }
            val studyPlanDeferred = async {
                currentStudyPlanStateRepository
                    .getState(forceUpdate = action.forceLoadFromNetwork)
            }

            val profile = profileDeferred.await().getOrElse {
                sentryInteractor.finishTransaction(transaction, throwable = it)
                onNewMessage(ProgressScreenFeature.TrackWithProgressFetchResult.Error)
                return@coroutineScope
            }
            val studyPlan = studyPlanDeferred.await().getOrElse {
                sentryInteractor.finishTransaction(transaction, throwable = it)
                onNewMessage(ProgressScreenFeature.TrackWithProgressFetchResult.Error)
                return@coroutineScope
            }

            if (studyPlan.trackId == null) {
                sentryInteractor.finishTransaction(transaction)
                onNewMessage(ProgressScreenFeature.TrackWithProgressFetchResult.Error)
                return@coroutineScope
            }

            val trackWithProgress = fetchTrackWithProgress(studyPlan.trackId, action.forceLoadFromNetwork)
                .getOrElse {
                    sentryInteractor.finishTransaction(transaction, throwable = it)
                    onNewMessage(ProgressScreenFeature.TrackWithProgressFetchResult.Error)
                    return@coroutineScope
                }

            if (trackWithProgress == null) {
                sentryInteractor.finishTransaction(transaction)
                onNewMessage(ProgressScreenFeature.TrackWithProgressFetchResult.Error)
                return@coroutineScope
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
    }

    private suspend fun handleFetchProjectWithProgressAction(
        action: InternalAction.FetchProjectWithProgress,
        onNewMessage: (Message) -> Unit
    ) {
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
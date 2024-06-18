package org.hyperskill.app.progress_screen.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature.InternalAction
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature.Message
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.domain.model.TrackWithProgress
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class ProgressScreenActionDispatcher(
    config: ActionDispatcherOptions,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val trackInteractor: TrackInteractor,
    private val projectsRepository: ProjectsRepository,
    private val progressesInteractor: ProgressesInteractor,
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
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchTrackWithProgressAction(
        action: InternalAction.FetchTrackWithProgress,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildProgressScreenRemoteTrackWithProgressLoading(),
            onError = { ProgressScreenFeature.TrackWithProgressFetchResult.Error }
        ) {
            coroutineScope {
                val profileDeferred = async {
                    currentProfileStateRepository
                        .getState(forceUpdate = action.forceLoadFromNetwork)
                }

                val subscriptionDeferred = async {
                    currentSubscriptionStateRepository
                        .getState(forceUpdate = action.forceLoadFromNetwork)
                }
                val studyPlanDeferred = async {
                    currentStudyPlanStateRepository
                        .getState(forceUpdate = action.forceLoadFromNetwork)
                }

                val profile = profileDeferred.await().getOrThrow()
                val subscription = subscriptionDeferred.await().getOrThrow()
                val studyPlan = studyPlanDeferred.await().getOrThrow()

                if (studyPlan.trackId == null) {
                    return@coroutineScope ProgressScreenFeature.TrackWithProgressFetchResult.Error
                }

                val trackWithProgress =
                    fetchTrackWithProgress(studyPlan.trackId, action.forceLoadFromNetwork)
                        .getOrThrow()
                        ?: return@coroutineScope ProgressScreenFeature.TrackWithProgressFetchResult.Error

                ProgressScreenFeature.TrackWithProgressFetchResult.Success(
                    trackWithProgress = trackWithProgress,
                    studyPlan = studyPlan,
                    profile = profile,
                    subscription = subscription
                )
            }
        }.let(onNewMessage)
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
        kotlin.runCatching {
            coroutineScope {
                val trackDeferred = async {
                    trackInteractor.getTrack(trackId, forceLoadFromRemote)
                }
                val trackProgressDeferred = async {
                    progressesInteractor.getTrackProgress(trackId, forceLoadFromRemote)
                }
                TrackWithProgress(
                    track = trackDeferred.await().getOrThrow(),
                    trackProgress = trackProgressDeferred.await().getOrThrow() ?: return@coroutineScope null
                )
            }
        }

    private suspend fun fetchProjectWithProgress(
        projectId: Long,
        forceLoadFromRemote: Boolean
    ): Result<ProjectWithProgress?> =
        kotlin.runCatching {
            coroutineScope {
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
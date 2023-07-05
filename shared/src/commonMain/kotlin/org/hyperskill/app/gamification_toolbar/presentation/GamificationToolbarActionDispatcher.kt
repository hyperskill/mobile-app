package org.hyperskill.app.gamification_toolbar.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Action
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.observeHypercoinsBalance
import org.hyperskill.app.progress_screen.domain.repository.ProgressesRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow
import org.hyperskill.app.streaks.domain.flow.StreakFlow
import org.hyperskill.app.streaks.domain.interactor.StreaksInteractor
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track.domain.repository.TrackRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class GamificationToolbarActionDispatcher(
    config: ActionDispatcherOptions,
    profileInteractor: ProfileInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val streaksInteractor: StreaksInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val streakFlow: StreakFlow,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    private val trackRepository: TrackRepository,
    private val progressesRepository: ProgressesRepository,
    topicCompletedFlow: TopicCompletedFlow
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        profileInteractor.solvedStepsSharedFlow
            .onEach { onNewMessage(Message.StepSolved) }
            .launchIn(actionScope)

        currentProfileStateRepository.observeHypercoinsBalance()
            .onEach { hypercoinsBalance ->
                onNewMessage(Message.HypercoinsBalanceChanged(hypercoinsBalance))
            }
            .launchIn(actionScope)

        streakFlow.observe()
            .distinctUntilChanged()
            .onEach { streak ->
                onNewMessage(Message.StreakChanged(streak))
            }
            .launchIn(actionScope)

        currentStudyPlanStateRepository.changes
            .distinctUntilChanged()
            .onEach { studyPlan ->
                onNewMessage(Message.StudyPlanChanged(studyPlan))
            }
            .launchIn(actionScope)

        topicCompletedFlow.observe()
            .distinctUntilChanged()
            .onEach {
                onNewMessage(Message.TopicCompleted)
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchGamificationToolbarData -> coroutineScope {
                val sentryTransaction = action.screen.fetchContentSentryTransaction
                sentryInteractor.startTransaction(sentryTransaction)

                val currentUserId = currentProfileStateRepository
                    .getState()
                    .map { it.id }
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(Message.FetchGamificationToolbarDataError)
                        return@coroutineScope
                    }

                val streakResult = async { streaksInteractor.getUserStreak(currentUserId) }
                val profileResult = async {
                    currentProfileStateRepository.getState(forceUpdate = true)
                }
                val trackWithProgressDeferred = async {
                    fetchTrackWithProgressThroughStudyPlan(action.forceUpdate)
                }

                val streak = streakResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    onNewMessage(Message.FetchGamificationToolbarDataError)
                    return@coroutineScope
                }
                val profile = profileResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    onNewMessage(Message.FetchGamificationToolbarDataError)
                    return@coroutineScope
                }
                val trackWithProgress = trackWithProgressDeferred.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    onNewMessage(Message.FetchGamificationToolbarDataError)
                    return@coroutineScope
                }

                sentryInteractor.finishTransaction(sentryTransaction)

                streakFlow.notifyDataChanged(streak)

                onNewMessage(
                    Message.FetchGamificationToolbarDataSuccess(
                        streak,
                        profile.gamification.hypercoinsBalance,
                        trackWithProgress
                    )
                )
            }
            is Action.FetchTrackWithProgress -> {
                sentryInteractor.startTransaction(action.transaction)
                fetchTrackWithProgress(action.trackId, true)
                    .fold(
                        onSuccess = { trackWithProgress ->
                            onNewMessage(Message.FetchTrackWithProgressResult.Success(trackWithProgress))
                            sentryInteractor.finishTransaction(action.transaction)
                        },
                        onFailure = {
                            onNewMessage(Message.FetchTrackWithProgressResult.Error)
                            sentryInteractor.finishTransaction(action.transaction, throwable = it)
                        }
                    )
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }

    private suspend fun fetchTrackWithProgressThroughStudyPlan(
        forceLoadFromRemote: Boolean
    ): Result<TrackWithProgress?> =
        kotlin.runCatching {
            val studyPlan =
                currentStudyPlanStateRepository.getState(forceLoadFromRemote).getOrThrow()
            if (studyPlan.trackId != null) {
                fetchTrackWithProgress(studyPlan.trackId, forceLoadFromRemote).getOrThrow()
            } else {
                null
            }
        }

    private suspend fun fetchTrackWithProgress(
        trackId: Long,
        forceLoadFromRemote: Boolean
    ): Result<TrackWithProgress?> =
        coroutineScope {
            kotlin.runCatching {
                val trackDeferred = async {
                    trackRepository.getTrack(trackId, forceLoadFromRemote)
                }
                val trackProgressDeferred = async {
                    progressesRepository
                        .getTrackProgress(trackId, forceLoadFromRemote)
                }
                TrackWithProgress(
                    track = trackDeferred.await().getOrThrow(),
                    trackProgress = trackProgressDeferred.await().getOrThrow() ?: return@runCatching null
                )
            }
        }
}
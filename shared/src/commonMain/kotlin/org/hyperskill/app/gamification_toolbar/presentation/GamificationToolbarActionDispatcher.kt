package org.hyperskill.app.gamification_toolbar.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Action
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
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
    private val profileInteractor: ProfileInteractor,
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

        profileInteractor.observeHypercoinsBalance()
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

                val currentUserId = profileInteractor
                    .getCurrentProfile()
                    .map { it.id }
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(Message.FetchGamificationToolbarDataError)
                        return@coroutineScope
                    }

                val streakResult = async { streaksInteractor.getUserStreak(currentUserId) }
                val profileResult = async {
                    profileInteractor.getCurrentProfile(sourceType = DataSourceType.REMOTE)
                }
                val trackWithProgressDeferred = async {
                    fetchTrackWithProgressThoughtStudyPlan(action.forceUpdate)
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
                fetchTrackWithProgress(action.trackId, true)
                    .fold(
                        onSuccess = { trackWithProgress ->
                            onNewMessage(Message.FetchTrackWithProgressResult.Success(trackWithProgress))
                        },
                        onFailure = {
                            onNewMessage(Message.FetchTrackWithProgressResult.Error)
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

    private suspend fun fetchTrackWithProgressThoughtStudyPlan(forceLoadFromRemote: Boolean): Result<TrackWithProgress?> =
        kotlin.runCatching {
            val studyPlan =
                currentStudyPlanStateRepository.getState(forceLoadFromRemote).getOrThrow()
            if (studyPlan.trackId != null) {
                fetchTrackWithProgress(studyPlan.trackId, forceLoadFromRemote).getOrThrow()
            } else {
                null
            }
        }

    private suspend fun fetchTrackWithProgress(trackId: Long, forceLoadFromRemote: Boolean): Result<TrackWithProgress?> =
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
                    trackProgress = trackProgressDeferred.await().getOrThrow()
                        ?: return@runCatching null
                )
            }
        }
}
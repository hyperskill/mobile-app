package org.hyperskill.app.gamification_toolbar.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Action
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.streaks.domain.interactor.StreaksInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class GamificationToolbarActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileInteractor: ProfileInteractor,
    private val streaksInteractor: StreaksInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
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

        profileInteractor.observeStreak()
            .distinctUntilChanged()
            .onEach { streak ->
                onNewMessage(Message.StreakChanged(streak))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchGamificationToolbarData -> {
                val sentryTransaction = action.screen.sentryTransaction
                sentryInteractor.startTransaction(sentryTransaction)

                val currentUserId = profileInteractor
                    .getCurrentProfile()
                    .map { it.id }
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        return onNewMessage(Message.FetchGamificationToolbarDataError)
                    }

                val streakResult = actionScope.async { streaksInteractor.getUserStreak(currentUserId) }
                val profileResult = actionScope.async {
                    profileInteractor.getCurrentProfile(sourceType = DataSourceType.REMOTE)
                }

                val streak = streakResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    return onNewMessage(Message.FetchGamificationToolbarDataError)
                }
                val profile = profileResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    return onNewMessage(Message.FetchGamificationToolbarDataError)
                }

                sentryInteractor.finishTransaction(sentryTransaction)

                profileInteractor.notifyStreakChanged(streak)

                onNewMessage(
                    Message.FetchGamificationToolbarDataSuccess(
                        streak,
                        profile.gamification.hypercoinsBalance
                    )
                )
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }
}
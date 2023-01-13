package org.hyperskill.app.navigation_bar_items.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsFeature.Action
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsFeature.Message
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.streaks.domain.interactor.StreaksInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class NavigationBarItemsActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileInteractor: ProfileInteractor,
    private val streaksInteractor: StreaksInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        actionScope.launch {
            profileInteractor.solvedStepsSharedFlow.collect {
                onNewMessage(Message.StepSolved)
            }
        }

        actionScope.launch {
            profileInteractor.observeHypercoinsBalance().collect {
                onNewMessage(Message.HypercoinsBalanceChanged(it))
            }
        }
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchNavigationBarItems -> {
                val sentryTransaction = action.screen.sentryTransaction
                sentryInteractor.startTransaction(sentryTransaction)

                val currentUserId = profileInteractor
                    .getCurrentProfile()
                    .map { it.id }
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        return onNewMessage(Message.NavigationBarItemsError)
                    }

                val streakResult = actionScope.async { streaksInteractor.getUserStreak(currentUserId) }
                val profileResult = actionScope.async {
                    profileInteractor.getCurrentProfile(sourceType = DataSourceType.REMOTE)
                }

                val streak = streakResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    return onNewMessage(Message.NavigationBarItemsError)
                }
                val profile = profileResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    return onNewMessage(Message.NavigationBarItemsError)
                }

                sentryInteractor.finishTransaction(sentryTransaction)

                onNewMessage(Message.NavigationBarItemsSuccess(streak, profile.gamification.hypercoinsBalance))
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }
}
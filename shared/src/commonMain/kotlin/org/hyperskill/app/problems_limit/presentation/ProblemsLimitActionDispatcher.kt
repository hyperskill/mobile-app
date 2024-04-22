package org.hyperskill.app.problems_limit.presentation

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Action
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.InternalAction
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.InternalMessage
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature.Message
import org.hyperskill.app.profile.domain.model.freemiumChargeLimitsStrategy
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProblemsLimitActionDispatcher(
    config: ActionDispatcherOptions,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val analyticInteractor: AnalyticInteractor,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        currentSubscriptionStateRepository
            .changes
            .distinctUntilChanged()
            .onEach {
                onNewMessage(InternalMessage.SubscriptionChanged(it))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.event)
            is InternalAction.FetchSubscription -> handleFetchSubscription(::onNewMessage)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchSubscription(onNewMessage: (Message) -> Unit) {
        val subscription = currentSubscriptionStateRepository.getState().getOrElse {
            logger.e(it) { "Failed to fetch subscription" }
            onNewMessage(InternalMessage.SubscriptionFetchError)
            return
        }
        val profile = currentProfileStateRepository.getState().getOrElse {
            logger.e(it) { "Failed to fetch profile" }
            onNewMessage(InternalMessage.SubscriptionFetchError)
            return
        }
        onNewMessage(
            InternalMessage.SubscriptionFetchSuccess(
                subscription = subscription,
                chargeLimitsStrategy = profile.freemiumChargeLimitsStrategy
            )
        )
    }
}
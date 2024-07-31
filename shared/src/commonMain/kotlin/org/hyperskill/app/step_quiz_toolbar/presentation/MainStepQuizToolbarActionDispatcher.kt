package org.hyperskill.app.step_quiz_toolbar.presentation

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.features.data.source.FeaturesDataSource
import org.hyperskill.app.profile.domain.model.freemiumChargeLimitsStrategy
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.Action
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.InternalAction
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.InternalMessage
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.Message
import org.hyperskill.app.subscriptions.domain.interactor.SubscriptionsInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class MainStepQuizToolbarActionDispatcher(
    config: ActionDispatcherOptions,
    private val subscriptionsInteractor: SubscriptionsInteractor,
    private val featuresDataSource: FeaturesDataSource,
    private val purchaseInteractor: PurchaseInteractor,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        subscriptionsInteractor
            .subscribeOnSubscriptionWithLimitType()
            .distinctUntilChanged()
            .onEach {
                onNewMessage(InternalMessage.SubscriptionChanged(it))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchSubscription -> handleFetchSubscription(::onNewMessage)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchSubscription(onNewMessage: (Message) -> Unit) {
        val features = featuresDataSource.getFeaturesMap()

        val canMakePayments = canMakePayments()

        val subscriptionWithLimitType =
            subscriptionsInteractor
                .getSubscriptionWithLimitType()
                .getOrElse {
                    logger.e(it) { "Failed to fetch subscription" }
                    onNewMessage(InternalMessage.SubscriptionFetchError)
                    return
                }

        onNewMessage(
            InternalMessage.SubscriptionFetchSuccess(
                subscription = subscriptionWithLimitType.subscription,
                subscriptionLimitType = subscriptionWithLimitType.subscriptionLimitType,
                chargeLimitsStrategy = features.freemiumChargeLimitsStrategy
            )
        )
    }

    private suspend fun canMakePayments(): Boolean =
        purchaseInteractor.canMakePayments().getOrDefault(false)
}
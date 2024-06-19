package org.hyperskill.app.welcome_onboarding.root.presentation

import co.touchlab.kermit.Logger
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.InternalAction
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.InternalMessage
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class WelcomeOnboardingActionDispatcher(
    config: ActionDispatcherOptions,
    private val sentryInteractor: SentryInteractor,
    private val learningActivityRepository: LearningActivitiesRepository,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchNextLearningActivity -> handleFetchNextLearningActivity(::onNewMessage)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchNextLearningActivity(onNewMessage: (Message) -> Unit) =
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildWelcomeOnboardingFeatureFetchNextLearningActivity(),
            onError = { e ->
                logger.e(e) { "Failed to fetch next learning activity" }
                InternalMessage.FetchNextLearningActivityError
            }
        ) {
            InternalMessage.FetchNextLearningActivitySuccess(
                learningActivityRepository
                    .getNextLearningActivity(types = setOf(LearningActivityType.LEARN_TOPIC))
                    .getOrThrow()
            )
        }.let(onNewMessage)
}
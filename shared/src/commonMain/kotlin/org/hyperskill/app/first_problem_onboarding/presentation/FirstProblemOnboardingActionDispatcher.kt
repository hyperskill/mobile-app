package org.hyperskill.app.first_problem_onboarding.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Action
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.FetchNextLearningActivityResult
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.FetchProfileResult
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.InternalAction
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Message
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class FirstProblemOnboardingActionDispatcher(
    config: ActionDispatcherOptions,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val learningActivityRepository: LearningActivitiesRepository,
    private val onboardingInteractor: OnboardingInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchProfile -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder
                    .buildFirstProblemOnboardingFeatureProfileDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val message = currentProfileStateRepository
                    .getState(forceUpdate = false)
                    .fold(
                        onSuccess = {
                            sentryInteractor.finishTransaction(sentryTransaction)
                            FetchProfileResult.Success(it)
                        },
                        onFailure = {
                            sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                            FetchProfileResult.Error
                        }
                    )

                onNewMessage(message)
            }
            InternalAction.FetchNextLearningActivity -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder
                    .buildFirstProblemOnboardingFeatureFetchNextLearningActivity()
                sentryInteractor.startTransaction(sentryTransaction)

                val message = learningActivityRepository
                    .getNextLearningActivity(types = setOf(LearningActivityType.LEARN_TOPIC))
                    .fold(
                        onSuccess = {
                            sentryInteractor.finishTransaction(sentryTransaction)
                            FetchNextLearningActivityResult.Success(it)
                        },
                        onFailure = {
                            sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                            FetchNextLearningActivityResult.Error
                        }
                    )

                onNewMessage(message)
            }
            is InternalAction.SetFirstProblemOnboardingShownFlag ->
                onboardingInteractor.setFirstProblemOnboardingWasShown(wasShown = true)
            else -> {
                // no op
            }
        }
    }
}
package org.hyperskill.app.first_problem_onboarding.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Action
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.FetchNextLearningActivityResult
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.FetchProfileResult
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.InternalAction
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Message
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class FirstProblemOnboardingActionDispatcher(
    config: ActionDispatcherOptions,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val learningActivityRepository: LearningActivitiesRepository,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchProfile -> {
                val message = currentProfileStateRepository
                    .getState()
                    .fold(
                        onSuccess = { FetchProfileResult.Success(it) },
                        onFailure = { FetchProfileResult.Error }
                    )

                onNewMessage(message)
            }
            InternalAction.FetchNextLearningActivity -> {
                val message = learningActivityRepository
                    .getNextLearningActivity(setOf(LearningActivityType.LEARN_TOPIC))
                    .fold(
                        onSuccess = { FetchNextLearningActivityResult.Success(it) },
                        onFailure = { FetchNextLearningActivityResult.Error }
                    )

                onNewMessage(message)
            }
            is InternalAction.LogAnalyticsEvent ->
                analyticInteractor.logEvent(action.event)
            else -> {
                // no op
            }
        }
    }
}
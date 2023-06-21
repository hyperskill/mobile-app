package org.hyperskill.app.onboarding.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Action
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Message
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class OnboardingActionDispatcher(
    config: ActionDispatcherOptions,
    private val onboardingInteractor: OnboardingInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchOnboarding -> {
                onboardingInteractor.setOnboardingShown(true)
                currentProfileStateRepository
                    .getState()
                    .fold(
                        onSuccess = { onNewMessage(Message.OnboardingSuccess(it)) },
                        onFailure = { onNewMessage(Message.OnboardingFailure) }
                    )
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {}
        }
    }
}
package org.hyperskill.app.onboarding.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Action
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class OnboardingActionDispatcher(
    config: ActionDispatcherOptions,
    private val onboardingInteractor: OnboardingInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchOnboarding -> {
                onboardingInteractor.setOnboardingShown(true)
            }
        }
    }
}
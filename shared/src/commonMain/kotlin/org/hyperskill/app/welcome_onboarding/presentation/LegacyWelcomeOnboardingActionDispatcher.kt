package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.welcome_onboarding.presentation.LegacyWelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.LegacyWelcomeOnboardingFeature.InternalAction
import org.hyperskill.app.welcome_onboarding.presentation.LegacyWelcomeOnboardingFeature.InternalMessage
import org.hyperskill.app.welcome_onboarding.presentation.LegacyWelcomeOnboardingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class LegacyWelcomeOnboardingActionDispatcher(
    config: ActionDispatcherOptions,
    private val onboardingInteractor: OnboardingInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchFirstProblemOnboardingData -> {
                onNewMessage(
                    InternalMessage.FirstProblemOnboardingDataFetched(
                        wasFirstProblemOnboardingShown = onboardingInteractor.wasFirstProblemOnboardingShown()
                    )
                )
            }
            else -> {
                // no op
            }
        }
    }
}
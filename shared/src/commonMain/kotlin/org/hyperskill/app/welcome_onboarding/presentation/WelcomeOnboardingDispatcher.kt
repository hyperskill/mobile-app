package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.InternalAction
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class WelcomeOnboardingDispatcher(
    config: ActionDispatcherOptions,
    private val onboardingInteractor: OnboardingInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchNotificationOnboardingData -> {
                onNewMessage(
                    Message.NotificationOnboardingDataFetched(
                        wasNotificationOnBoardingShown = onboardingInteractor.wasNotificationOnboardingShown()
                    )
                )
            }
            InternalAction.FetchFirstProblemOnboardingData -> {
                onNewMessage(
                    Message.FirstProblemOnboardingDataFetched(
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
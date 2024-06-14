package org.hyperskill.app.welcome_onboarding.root.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.InternalAction
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class WelcomeOnboardingActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.event)
            else -> {
                // no op
            }
        }
    }
}
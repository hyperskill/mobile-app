package org.hyperskill.app.users_questionnaire.onboarding.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.users_questionnaire.onboarding.presentation.UsersQuestionnaireOnboardingFeature.Action
import org.hyperskill.app.users_questionnaire.onboarding.presentation.UsersQuestionnaireOnboardingFeature.InternalAction
import org.hyperskill.app.users_questionnaire.onboarding.presentation.UsersQuestionnaireOnboardingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class UsersQuestionnaireOnboardingActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.event, action.forceLogEvent)
            else -> {
                // no op
            }
        }
    }
}
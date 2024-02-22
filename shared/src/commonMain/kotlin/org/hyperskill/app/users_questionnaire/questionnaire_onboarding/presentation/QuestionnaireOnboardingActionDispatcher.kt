package org.hyperskill.app.users_questionnaire.questionnaire_onboarding.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.users_questionnaire.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.Action
import org.hyperskill.app.users_questionnaire.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.InternalAction
import org.hyperskill.app.users_questionnaire.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class QuestionnaireOnboardingActionDispatcher(
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
package org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Action
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.InternalAction
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

@Deprecated("Should be removed in ALTAPPS-1276")
internal class LegacyUsersQuestionnaireOnboardingActionDispatcher(
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
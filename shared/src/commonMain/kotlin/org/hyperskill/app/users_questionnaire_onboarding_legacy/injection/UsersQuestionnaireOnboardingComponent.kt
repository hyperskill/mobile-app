package org.hyperskill.app.users_questionnaire_onboarding_legacy.injection

import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Action
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Message
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

@Deprecated("Should be removed in ALTAPPS-1276")
interface UsersQuestionnaireOnboardingComponent {
    val usersQuestionnaireOnboardingFeature: Feature<ViewState, Message, Action>
}
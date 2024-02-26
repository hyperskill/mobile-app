package org.hyperskill.app.users_questionnaire.onboarding.injection

import org.hyperskill.app.users_questionnaire.onboarding.presentation.UsersQuestionnaireOnboardingFeature.Action
import org.hyperskill.app.users_questionnaire.onboarding.presentation.UsersQuestionnaireOnboardingFeature.Message
import org.hyperskill.app.users_questionnaire.onboarding.presentation.UsersQuestionnaireOnboardingFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface UsersQuestionnaireOnboardingComponent {
    val usersQuestionnaireOnboardingFeature: Feature<ViewState, Message, Action>
}
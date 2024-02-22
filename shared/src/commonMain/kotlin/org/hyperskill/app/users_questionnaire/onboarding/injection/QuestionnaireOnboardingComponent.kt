package org.hyperskill.app.users_questionnaire.onboarding.injection

import org.hyperskill.app.users_questionnaire.onboarding.presentation.QuestionnaireOnboardingFeature.Action
import org.hyperskill.app.users_questionnaire.onboarding.presentation.QuestionnaireOnboardingFeature.Message
import org.hyperskill.app.users_questionnaire.onboarding.presentation.QuestionnaireOnboardingFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface QuestionnaireOnboardingComponent {
    val questionnaireOnboardingFeature: Feature<ViewState, Message, Action>
}
package org.hyperskill.app.questionnaire_onboarding.injection

import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.Action
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.Message
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface QuestionnaireOnboardingComponent {
    val questionnaireOnboardingFeature: Feature<ViewState, Message, Action>
}
package org.hyperskill.app.welcome_onboarding.questionnaire.view

import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireItemType

data class WelcomeQuestionnaireItem(
    val type: WelcomeQuestionnaireItemType,
    val text: String
)
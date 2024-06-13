package org.hyperskill.app.welcome_onboarding.view

import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireItemType

data class WelcomeQuestionnaireItem(
    val type: WelcomeQuestionnaireItemType,
    val text: String
)

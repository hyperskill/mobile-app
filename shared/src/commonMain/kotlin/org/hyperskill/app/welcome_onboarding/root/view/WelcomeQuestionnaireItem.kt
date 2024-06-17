package org.hyperskill.app.welcome_onboarding.root.view

import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireItemType

data class WelcomeQuestionnaireItem(
    val type: WelcomeQuestionnaireItemType,
    val text: String
)
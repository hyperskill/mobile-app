package org.hyperskill.app.android.welcome_onbaording.model

import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireType
import org.hyperskill.app.welcome_onboarding.view.WelcomeQuestionnaireItemType

interface WelcomeOnboardingHost {
    fun onStartClick()
    fun onQuestionnaireItemClicked(
        questionnaireType: WelcomeQuestionnaireType,
        itemType: WelcomeQuestionnaireItemType
    )
}
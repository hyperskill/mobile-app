package org.hyperskill.app.android.welcome_onbaording.model

import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireType

interface WelcomeOnboardingHost {
    fun onStartClick()
    fun onQuestionnaireItemClicked(
        questionnaireType: WelcomeQuestionnaireType,
        itemType: WelcomeQuestionnaireItemType
    )
    fun onProgrammingLanguageSelected(language: WelcomeOnboardingProgrammingLanguage)
    fun onTrackSelected()
}
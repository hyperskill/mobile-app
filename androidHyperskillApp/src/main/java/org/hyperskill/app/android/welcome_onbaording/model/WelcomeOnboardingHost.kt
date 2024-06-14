package org.hyperskill.app.android.welcome_onbaording.model

import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireType

interface WelcomeOnboardingHost {
    fun onStartClick()
    fun onQuestionnaireItemClicked(
        questionnaireType: WelcomeQuestionnaireType,
        itemType: WelcomeQuestionnaireItemType
    )
    fun onProgrammingLanguageSelected(language: WelcomeOnboardingProgrammingLanguage)
    fun onTrackSelected(track: WelcomeOnboardingTrack)
}
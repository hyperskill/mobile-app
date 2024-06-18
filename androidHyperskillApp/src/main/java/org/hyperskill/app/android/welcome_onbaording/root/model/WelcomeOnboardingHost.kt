package org.hyperskill.app.android.welcome_onbaording.root.model

import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireType
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingProgrammingLanguage

interface WelcomeOnboardingHost {
    fun onStartClick()
    fun onQuestionnaireItemClicked(
        questionnaireType: WelcomeQuestionnaireType,
        itemType: WelcomeQuestionnaireItemType
    )
    fun onProgrammingLanguageSelected(language: WelcomeOnboardingProgrammingLanguage)
    fun onTrackSelected(track: WelcomeOnboardingTrack)
    fun onNotificationOnboardingCompleted()
    fun onFinishClicked()

    fun onStartOnboardingViewed()
    fun onUserQuestionnaireViewed(questionnaireType: WelcomeQuestionnaireType)
    fun onSelectProgrammingLanguageViewed()
    fun onFinishScreenViewed()
}
package org.hyperskill.app.android.users_questionnaire_onboarding.ui

import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature

object UsersQuestionnaireOnboardingPreviewDefault {

    private enum class SelectedChoice {
        NONE,
        FIRST,
        LAST
    }

    private fun getViewState(
        selectedChoice: SelectedChoice,
        isSendButtonEnabled: Boolean
    ) =
        LegacyUsersQuestionnaireOnboardingFeature.ViewState(
            title = "How did you hear about Hyperskill?",
            choices = listOf(
                "Google",
                "Youtube",
                "Instagram",
                "Tiktok",
                "News",
                "Friends",
                "Other"
            ),
            selectedChoice = when (selectedChoice) {
                SelectedChoice.NONE -> null
                SelectedChoice.FIRST -> "Google"
                SelectedChoice.LAST -> "Other"
            },
            textInputValue = when (selectedChoice) {
                SelectedChoice.NONE -> null
                SelectedChoice.FIRST,
                SelectedChoice.LAST -> "example text"
            },
            isTextInputVisible = selectedChoice == SelectedChoice.LAST,
            isSendButtonEnabled = when (selectedChoice) {
                SelectedChoice.NONE -> false
                SelectedChoice.FIRST,
                SelectedChoice.LAST -> isSendButtonEnabled
            }
        )

    fun getUnselectedViewState(): LegacyUsersQuestionnaireOnboardingFeature.ViewState =
        getViewState(SelectedChoice.NONE, false)

    fun getFirstOptionSelectedViewState(): LegacyUsersQuestionnaireOnboardingFeature.ViewState =
        getViewState(SelectedChoice.FIRST, true)

    fun getOtherOptionSelectedViewState(isSendButtonEnabled: Boolean): LegacyUsersQuestionnaireOnboardingFeature.ViewState =
        getViewState(SelectedChoice.LAST, isSendButtonEnabled)
}
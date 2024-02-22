package org.hyperskill.app.users_questionnaire.questionnaire_onboarding.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.domain.platform.PlatformType
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.users_questionnaire.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.State
import org.hyperskill.app.users_questionnaire.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.ViewState

internal class QuestionnaireOnboardingViewStateMapper(
    platform: Platform,
    resourceProvider: ResourceProvider
) {
    private val title = resourceProvider.getString(
        SharedResources.strings.questionnaire_onboarding_title_template,
        resourceProvider.getString(platform.appNameResource)
    )

    private val choices = listOf(
        when (platform.platformType) {
            PlatformType.IOS ->
                resourceProvider.getString(SharedResources.strings.questionnaire_onboarding_source_app_store)
            PlatformType.ANDROID ->
                resourceProvider.getString(SharedResources.strings.questionnaire_onboarding_source_google_play)
        },
        resourceProvider.getString(SharedResources.strings.questionnaire_onboarding_source_google_search),
        resourceProvider.getString(SharedResources.strings.questionnaire_onboarding_source_youtube),
        resourceProvider.getString(SharedResources.strings.questionnaire_onboarding_source_instagram),
        resourceProvider.getString(SharedResources.strings.questionnaire_onboarding_source_tiktok),
        resourceProvider.getString(SharedResources.strings.questionnaire_onboarding_source_news),
        resourceProvider.getString(SharedResources.strings.questionnaire_onboarding_source_friends),
        resourceProvider.getString(SharedResources.strings.questionnaire_onboarding_source_other)
    )

    fun mapState(state: State): ViewState {
        val isTextInputVisible = state.selectedChoice == choices.last()

        val isSendButtonEnabled = if (isTextInputVisible) {
            state.textInputValue?.isNotBlank() == true
        } else {
            state.selectedChoice != null
        }

        return ViewState(
            title = title,
            choices = choices,
            selectedChoice = state.selectedChoice,
            textInputValue = state.textInputValue.takeIf { isTextInputVisible },
            isTextInputVisible = isTextInputVisible,
            isSendButtonEnabled = isSendButtonEnabled
        )
    }
}
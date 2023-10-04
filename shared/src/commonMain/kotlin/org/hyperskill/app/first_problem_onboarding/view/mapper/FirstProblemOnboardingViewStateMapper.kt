package org.hyperskill.app.first_problem_onboarding.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature

internal class FirstProblemOnboardingViewStateMapper(
    private val resourceProvider: ResourceProvider,
) {
    fun map(state: FirstProblemOnboardingFeature.State): FirstProblemOnboardingFeature.ViewState =
        when (state.profileState) {
            FirstProblemOnboardingFeature.ProfileState.Idle ->
                FirstProblemOnboardingFeature.ViewState.Idle
            FirstProblemOnboardingFeature.ProfileState.Loading ->
                FirstProblemOnboardingFeature.ViewState.Loading
            FirstProblemOnboardingFeature.ProfileState.Error ->
                FirstProblemOnboardingFeature.ViewState.Error
            is FirstProblemOnboardingFeature.ProfileState.Content ->
                FirstProblemOnboardingFeature.ViewState.Content(
                    subtitle = resourceProvider.getString(
                        if (state.isNewUserMode) {
                            SharedResources.strings.first_problem_onboarding_new_user_subtitle
                        } else {
                            SharedResources.strings.first_problem_onboarding_existing_user_subtitle
                        },
                        state.profileState.profile.trackTitle ?: ""
                    ),
                    isNewUserMode = state.isNewUserMode,
                    isLearningActivityLoading = state.isLearningActivityLoading
                )
        }
}
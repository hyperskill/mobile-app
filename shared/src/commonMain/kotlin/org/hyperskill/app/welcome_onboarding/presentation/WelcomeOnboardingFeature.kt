package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingStartScreen

object WelcomeOnboardingFeature {

    data class State(val initialStep: WelcomeOnboardingStartScreen)

    fun shouldLaunchFeature(profile: Profile, isNotificationPermissionGranted: Boolean): Boolean =
        profile.isNewUser || !isNotificationPermissionGranted

    internal fun initialState(params: WelcomeOnboardingFeatureParams) =
        State(
            initialStep = when {
                params.profile.isNewUser -> WelcomeOnboardingStartScreen.START_SCREEN
                !params.isNotificationPermissionGranted -> WelcomeOnboardingStartScreen.NOTIFICATION_ONBOARDING
                else -> error("Welcome onboarding should not be shown")
            }
        )

    sealed interface Message {
        object StartJourneyClicked : Message
    }

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object WelcomeOnboardingQuestionnaire : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction
    }
}
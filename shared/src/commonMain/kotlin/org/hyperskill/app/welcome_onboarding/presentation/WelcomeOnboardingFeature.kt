package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingStep

object WelcomeOnboardingFeature {

    data class State(val initialStep: WelcomeOnboardingStep)

    fun shouldLaunchFeature(profile: Profile, isNotificationPermissionGranted: Boolean): Boolean =
        profile.isNewUser || isNotificationPermissionGranted

    internal fun initialState() =
        State(initialStep = WelcomeOnboardingStep.START_SCREEN)

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
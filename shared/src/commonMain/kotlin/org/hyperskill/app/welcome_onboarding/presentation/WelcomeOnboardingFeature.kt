package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isNewUser
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingStartScreen
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireType

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
        data class QuestionnaireItemClicked(
            val questionnaireType: WelcomeQuestionnaireType,
            val itemType: WelcomeQuestionnaireItemType
        ) : Message
        data class ProgrammingLanguageSelected(val language: WelcomeOnboardingProgrammingLanguage) : Message
        object TrackSelected : Message
    }

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class WelcomeOnboardingQuestionnaire(val type: WelcomeQuestionnaireType) : NavigateTo
                object ChooseProgrammingLanguage : NavigateTo
                data class TrackDetails(val track: WelcomeOnboardingTrack) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction
    }
}
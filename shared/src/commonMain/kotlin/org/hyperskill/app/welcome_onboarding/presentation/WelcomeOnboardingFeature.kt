package org.hyperskill.app.welcome_onboarding.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.step.domain.model.StepRoute

interface WelcomeOnboardingFeature {

    @Serializable
    data class State(val profile: Profile? = null)
    
    sealed interface Message {
        data class OnboardingFlowRequested(
            val profile: Profile,
            val isNotificationPermissionGranted: Boolean
        ) : Message

        data class NotificationOnboardingDataFetched(val wasNotificationOnBoardingShown: Boolean) : Message
        object NotificationOnboardingCompleted : Message

        data class FirstProblemOnboardingDataFetched(val wasFirstProblemOnboardingShown: Boolean) : Message
        data class FirstProblemOnboardingCompleted(val firstProblemStepRoute: StepRoute?) : Message
    }

    sealed interface OnboardingFlowFinishReason {
        data class NotificationOnboardingFinished(val profile: Profile?) : OnboardingFlowFinishReason
        object FirstProblemOnboardingFinished : OnboardingFlowFinishReason
    }

    sealed interface Action {
        object FetchNotificationOnboardingData : Action
        object FetchFirstProblemOnboardingData : Action

        data class OnboardingFlowFinished(
            val reason: OnboardingFlowFinishReason
        ) : Action
    }

    sealed interface ViewAction : Action {
        sealed interface NavigateTo : ViewAction {
            object NotificationOnBoardingScreen : NavigateTo

            data class FirstProblemOnBoardingScreen(val isNewUserMode: Boolean) : NavigateTo

            data class StudyPlanWithStep(val stepRoute: StepRoute) : NavigateTo
        }
    }
}

fun Set<Action>.getFinishAction(): Action.OnboardingFlowFinished? =
    filterIsInstance<Action.OnboardingFlowFinished>().firstOrNull()
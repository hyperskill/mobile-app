package org.hyperskill.app.welcome_onboarding.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action

object WelcomeOnboardingFeature {
    @Serializable
    data class State(val profile: Profile? = null)

    sealed interface Message {
        object NotificationOnboardingCompleted : Message

        data class FirstProblemOnboardingCompleted(val firstProblemStepRoute: StepRoute?) : Message
    }

    internal sealed interface InternalMessage : Message {
        data class OnboardingFlowRequested(
            val profile: Profile,
            val isNotificationPermissionGranted: Boolean
        ) : InternalMessage

        data class FirstProblemOnboardingDataFetched(
            val wasFirstProblemOnboardingShown: Boolean
        ) : InternalMessage
    }

    sealed interface OnboardingFlowFinishReason {
        data class NotificationOnboardingFinished(val profile: Profile?) : OnboardingFlowFinishReason
        object FirstProblemOnboardingFinished : OnboardingFlowFinishReason
    }

    sealed interface Action {
        data class OnboardingFlowFinished(
            val reason: OnboardingFlowFinishReason
        ) : Action

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object NotificationOnboardingScreen : NavigateTo

                data class FirstProblemOnboardingScreen(val isNewUserMode: Boolean) : NavigateTo

                data class StudyPlanWithStep(val stepRoute: StepRoute) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        object FetchFirstProblemOnboardingData : InternalAction
    }
}

internal fun Set<Action>.getFinishAction(): Action.OnboardingFlowFinished? =
    filterIsInstance<Action.OnboardingFlowFinished>().firstOrNull()
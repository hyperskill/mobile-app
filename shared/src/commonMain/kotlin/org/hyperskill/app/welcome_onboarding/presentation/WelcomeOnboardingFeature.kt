package org.hyperskill.app.welcome_onboarding.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action

object WelcomeOnboardingFeature {

    @Serializable
    data class State(
        val profile: Profile? = null
    )

    sealed interface Message {
        data class OnboardingFlowRequested(
            val profile: Profile,
            val isNotificationPermissionGranted: Boolean
        ) : InternalMessage
        object NotificationOnboardingCompleted : Message

        object PaywallCompleted : Message

        data class FirstProblemOnboardingCompleted(val firstProblemStepRoute: StepRoute?) : Message
    }

    internal sealed interface InternalMessage : Message {

        data class NotificationOnboardingDataFetched(
            val wasNotificationOnboardingShown: Boolean
        ) : InternalMessage

        data class FirstProblemOnboardingDataFetched(
            val wasFirstProblemOnboardingShown: Boolean
        ) : InternalMessage

        data class FetchSubscriptionSuccess(val subscription: Subscription): InternalMessage

        object FetchSubscriptionError : InternalMessage
    }

    sealed interface Action {
        data class OnboardingFlowFinished(val profile: Profile?) : Action

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object NotificationOnboardingScreen : NavigateTo

                data class FirstProblemOnboardingScreen(val isNewUserMode: Boolean) : NavigateTo

                data class StudyPlanWithStep(val stepRoute: StepRoute) : NavigateTo

                object Paywall : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        object FetchNotificationOnboardingData : InternalAction
        object FetchFirstProblemOnboardingData : InternalAction
        object FetchSubscription : InternalAction
    }
}

internal fun Set<Action>.getFinishAction(): Action.OnboardingFlowFinished? =
    filterIsInstance<Action.OnboardingFlowFinished>().firstOrNull()
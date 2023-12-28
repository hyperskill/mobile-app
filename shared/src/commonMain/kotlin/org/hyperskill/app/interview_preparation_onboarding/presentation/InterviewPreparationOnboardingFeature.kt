package org.hyperskill.app.interview_preparation_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.StepRoute

object InterviewPreparationOnboardingFeature {
    data class State(val stepRoute: StepRoute)

    fun initialState(stepRoute: StepRoute): State =
        State(stepRoute)

    sealed interface Message {
        object ViewedEventMessage : Message

        object GoToFirstProblemClicked : Message
    }

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class StepScreen(val stepRoute: StepRoute) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticsEvent(val event: AnalyticEvent) : InternalAction

        object MarkOnboardingAsViewed : InternalAction
    }
}
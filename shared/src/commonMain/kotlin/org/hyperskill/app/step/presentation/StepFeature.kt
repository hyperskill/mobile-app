package org.hyperskill.app.step.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.comments.screen.domain.model.CommentsScreenFeatureParams
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step.domain.model.StepMenuAction
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature

object StepFeature {
    data class State(
        val stepState: StepState,
        val stepToolbarState: StepToolbarFeature.State,
        val isLoadingShowed: Boolean
    )

    internal fun initialState(stepRoute: StepRoute): State =
        State(
            stepState = StepState.Idle,
            stepToolbarState = StepToolbarFeature.initialState(stepRoute),
            isLoadingShowed = false
        )

    data class ViewState(
        val stepState: StepState,
        val stepToolbarViewState: StepToolbarFeature.ViewState,
        val stepMenuActions: Set<StepMenuAction>,
        val isCommentsToolbarItemAvailable: Boolean,
        val isLoadingShowed: Boolean
    )

    sealed interface StepState {
        object Idle : StepState
        object Loading : StepState
        object Error : StepState
        data class Data(
            val step: Step,
            val isPracticingAvailable: Boolean,
            val stepCompletionState: StepCompletionFeature.State
        ) : StepState
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message

        sealed interface StepLoaded : Message {
            data class Success(val step: Step) : StepLoaded
            object Error : StepLoaded
        }

        object ScreenShowed : Message
        object ScreenHidden : Message

        object CommentClicked : Message
        object ShareClicked : Message
        object ReportClicked : Message
        object SkipClicked : Message
        object OpenInWebClicked : Message

        /**
         * Message Wrappers
         */
        data class StepCompletionMessage(val message: StepCompletionFeature.Message) : Message

        data class StepToolbarMessage(val message: StepToolbarFeature.Message) : Message
    }

    internal sealed interface InternalMessage : Message {
        data class StepCompleted(val stepId: Long) : InternalMessage

        object SolvingTimerFired : InternalMessage

        data class ShareLinkReady(val link: String) : InternalMessage

        data class GetMagicLinkReceiveSuccess(val url: String) : InternalMessage
        object GetMagicLinkReceiveFailure : InternalMessage

        object StepSkipSuccess : InternalMessage
        object StepSkipFailed : InternalMessage

        data class FetchNextRecommendedStepSuccess(val nextRecommendedStep: Step) : InternalMessage
        object FetchNextRecommendedStepError : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class ShareStepLink(val link: String) : ViewAction

            data class ShowFeedbackModal(val stepRoute: StepRoute) : ViewAction

            data class OpenUrl(val url: String) : ViewAction

            object ShowLoadingError : ViewAction

            data class ReloadStep(val stepRoute: StepRoute) : ViewAction

            object ShowCantSkipError : ViewAction

            sealed interface NavigateTo : ViewAction {
                data class CommentsScreen(val params: CommentsScreenFeatureParams) : NavigateTo
            }

            data class StepCompletionViewAction(
                val viewAction: StepCompletionFeature.Action.ViewAction
            ) : ViewAction

            data class StepToolbarViewAction(
                val viewAction: StepToolbarFeature.Action.ViewAction
            ) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchStep(val stepRoute: StepRoute) : InternalAction

        data class ViewStep(val stepId: Long, val stepContext: StepContext) : InternalAction

        object StartSolvingTimer : InternalAction
        object StopSolvingTimer : InternalAction
        data class LogSolvingTime(val stepId: Long) : InternalAction

        data class UpdateNextLearningActivityState(val step: Step) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction

        data class CreateStepShareLink(val stepRoute: StepRoute) : InternalAction

        data class GetMagicLink(val path: HyperskillUrlPath) : InternalAction

        data class SkipStep(val stepId: Long) : InternalAction
        data class FetchNextRecommendedStep(val currentStep: Step) : InternalAction

        /**
         * Action Wrappers
         */
        data class StepCompletionAction(val action: StepCompletionFeature.Action) : InternalAction

        data class StepToolbarAction(val action: StepToolbarFeature.Action) : InternalAction
    }
}
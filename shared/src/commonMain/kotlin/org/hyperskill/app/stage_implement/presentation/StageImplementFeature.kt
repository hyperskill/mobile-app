package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.stages.domain.model.Stage
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute

object StageImplementFeature {
    internal sealed interface State {
        object Idle : State
        object Loading : State
        data class Deprecated(val project: Project) : State
        object Unsupported : State
        object NetworkError : State
        data class Content(
            val project: Project,
            val stage: Stage,
            val step: Step
        ) : State
    }

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        data class Deprecated(val message: String, val ctaButtonText: String) : ViewState
        object Unsupported : ViewState
        object NetworkError : ViewState
        data class Content(
            val stepRoute: StepRoute,
            val navigationTitle: String,
            val stageTitle: String
        ) : ViewState
    }

    sealed interface Message {
        data class Initialize(
            val projectId: Long,
            val stageId: Long,
            val forceUpdate: Boolean = false
        ) : Message

        sealed interface FetchStageImplementResult : Message {
            data class Deprecated(val project: Project) : FetchStageImplementResult
            object Unsupported : FetchStageImplementResult
            object NetworkError : FetchStageImplementResult
            data class Success(
                val project: Project,
                val stage: Stage,
                val step: Step
            ) : FetchStageImplementResult
        }

        object ProjectDeprecatedButtonClicked : Message

        /**
         * Unsupported modal
         */
        object UnsupportedModalShownEventMessage : Message
        object UnsupportedModalHiddenEventMessage : Message
        object UnsupportedModalGoToHomeScreenClicked : Message

        object ViewedEventMessage : Message
    }

    sealed interface Action {
        data class FetchStageImplement(val projectId: Long, val stageId: Long) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            object ShowUnsupportedModal : ViewAction

            sealed interface NavigateTo : ViewAction {
                object Back : NavigateTo
                object HomeScreen : NavigateTo
            }
        }
    }
}
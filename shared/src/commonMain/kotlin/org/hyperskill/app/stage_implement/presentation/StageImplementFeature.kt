package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.stages.domain.model.Stage
import org.hyperskill.app.step.domain.model.Step

object StageImplementFeature {
    internal sealed interface State {
        object Idle : State
        object Loading : State
        object Deprecated : State
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
        object Deprecated : ViewState
        object Unsupported : ViewState
        object NetworkError : ViewState
        data class Content(
            val project: Project,
            val stage: Stage,
            val step: Step
        ) : ViewState
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message
        sealed interface FetchStageImplementResult : Message {
            object Deprecated : FetchStageImplementResult
            object Unsupported : FetchStageImplementResult
            object NetworkError : FetchStageImplementResult
            data class Success(
                val project: Project,
                val stage: Stage,
                val step: Step
            ) : FetchStageImplementResult
        }
    }

    sealed interface Action {
        data class FetchStageImplement(val projectId: Long, val stageId: Long) : Action

        sealed interface ViewAction : Action
    }
}
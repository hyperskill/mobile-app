package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.ProjectCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.ProjectCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.ProjectCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementViewedHyperskillAnalyticEvent
import org.hyperskill.app.stages.domain.model.Stage
import org.hyperskill.app.step.domain.model.StepRoute

object StageImplementFeature {
    /**
     * Represents an internal feature state.
     */
    internal sealed interface State {
        object Idle : State
        object Loading : State
        object NetworkError : State

        /**
         * Represents a content state, when all necessary data is successfully loaded.
         *
         * @property projectId Current stage implementation project id
         * @property stage Current stage implementation stage
         */
        data class Content(val projectId: Long, val stage: Stage) : State
    }

    /**
     * Represents a public feature state, what is used for UI.
     */
    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object NetworkError : ViewState

        /**
         * Represents a content state, when all necessary data is successfully loaded.
         *
         * @property stepRoute Step route to inject into the step feature
         * @property navigationTitle Navigation title to be displayed
         * @property stageTitle Stage title to be displayed
         */
        data class Content(
            val stepRoute: StepRoute,
            val navigationTitle: String,
            val stageTitle: String
        ) : ViewState
    }

    sealed interface Message {
        /**
         * Represents a message, when feature is initializing. Initiates data loading.
         *
         * @property projectId Project id
         * @property stageId Stage id
         * @property forceUpdate Flag, that indicates if we should force data loading
         *
         * @see InternalMessage.FetchStageImplementSuccess
         * @see InternalMessage.FetchStageImplementFailure
         * @see InternalAction.FetchStageImplement
         */
        data class Initialize(
            val projectId: Long,
            val stageId: Long,
            val forceUpdate: Boolean = false
        ) : Message

        /**
         * Represents a stage completed modal clicked "Go to home screen" event message.
         *
         * @see StageCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent
         * @see Action.ViewAction.NavigateTo.StudyPlan
         */
        object StageCompletedModalGoToStudyPlanClicked : Message

        /**
         * Represents a project completed modal clicked "Go to home screen" event message.
         *
         * @see ProjectCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent
         * @see Action.ViewAction.NavigateTo.StudyPlan
         */
        object ProjectCompletedModalGoToStudyPlanClicked : Message

        /**
         * Represents a feature view analytic event message.
         *
         * @see StageImplementViewedHyperskillAnalyticEvent
         */
        object ViewedEventMessage : Message

        /**
         * Represents a stage completed modal shown analytic event message.
         *
         * @see StageCompletedModalShownHyperskillAnalyticEvent
         */
        object StageCompletedModalShownEventMessage : Message

        /**
         * Represents a stage completed modal hidden analytic event message.
         *
         * @see StageCompletedModalHiddenHyperskillAnalyticEvent
         */
        object StageCompletedModalHiddenEventMessage : Message

        /**
         * Represents a project completed modal shown analytic event message.
         *
         * @see ProjectCompletedModalShownHyperskillAnalyticEvent
         */
        object ProjectCompletedModalShownEventMessage : Message

        /**
         * Represents a project completed modal hidden analytic event message.
         *
         * @see ProjectCompletedModalHiddenHyperskillAnalyticEvent
         */
        object ProjectCompletedModalHiddenEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        /**
         * Represents a message, when feature initializing failed.
         *
         * @see State.NetworkError
         */
        object FetchStageImplementFailure : InternalMessage

        /**
         * Represents a message, when feature initializing succeeded.
         *
         * @see State.Content
         */
        data class FetchStageImplementSuccess(val projectId: Long, val stage: Stage) : InternalMessage

        /**
         * Represents a message, when step is solved. Checks that solved step equals stage step.
         *
         * @see InternalAction.CheckStageCompletionStatus
         */
        data class StepSolved(val stepId: Long) : InternalMessage

        /**
         * Represents a message, when stage is completed
         *
         * @property title Title of stage completed modal
         * @property stageAward Gems award for stage completion
         *
         * @see Action.ViewAction.ShowStageCompletedModal
         */
        data class StageCompleted(val title: String, val stageAward: Int) : InternalMessage

        /**
         * Represents a message, when project is completed
         *
         * @property stageAward Gems award for stage completion
         * @property projectAward Gems award for project completion
         *
         * @see Action.ViewAction.ShowProjectCompletedModal
         */
        data class ProjectCompleted(val stageAward: Int, val projectAward: Int) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            /**
             * Represents a view action to show stage completion modal
             *
             * @property title Title of stage completed modal
             * @property stageAward Gems award for stage completion
             *
             * @see InternalMessage.StageCompleted
             */
            data class ShowStageCompletedModal(val title: String, val stageAward: Int) : ViewAction

            /**
             * Represents a view action to show project completion modal
             *
             * @property stageAward Gems award for stage completion
             * @property projectAward Gems award for project completion
             *
             * @see InternalMessage.ProjectCompleted
             */
            data class ShowProjectCompletedModal(val stageAward: Int, val projectAward: Int) : ViewAction

            sealed interface NavigateTo : ViewAction {
                object StudyPlan : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        /**
         * Represents an action, when feature is initializing. Initiates data loading.
         *
         * @property projectId Project id to load
         * @property stageId Stage id to load
         *
         * @see Message.Initialize
         * @see InternalMessage.FetchStageImplementSuccess
         * @see InternalMessage.FetchStageImplementFailure
         */
        data class FetchStageImplement(val projectId: Long, val stageId: Long) : InternalAction

        /**
         * Represents an action, that checks stage completion
         *
         * @property stage Current stage
         *
         * @see InternalMessage.StageCompleted
         * @see InternalMessage.ProjectCompleted
         */
        data class CheckStageCompletionStatus(val stage: Stage) : InternalAction

        /**
         * Represents an analytic action, logs analytic event to the analytic service.
         *
         * @property analyticEvent Analytic event to be logged
         */
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}
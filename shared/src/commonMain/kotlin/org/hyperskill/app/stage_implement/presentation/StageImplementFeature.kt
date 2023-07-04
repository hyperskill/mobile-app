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
         * @see FetchStageImplementResult
         * @see Action.FetchStageImplement
         */
        data class Initialize(
            val projectId: Long,
            val stageId: Long,
            val forceUpdate: Boolean = false
        ) : Message

        /**
         * Represents an available results of the feature data loading.
         * @see Initialize
         */
        sealed interface FetchStageImplementResult : Message {
            /**
             * Represents a network error result.
             *
             * @see State.NetworkError
             */
            object NetworkError : FetchStageImplementResult

            /**
             * Represents a success result, when all necessary data is successfully loaded.
             *
             * @property projectId Current stage implementation project id
             * @property stage Current stage implementation stage
             *
             * @see State.Content
             */
            data class Success(val projectId: Long, val stage: Stage) : FetchStageImplementResult
        }

        /**
         * Represents a message, when step is solved. Checks that solved step equals stage step
         *
         * @see Action.CheckStageCompletionStatus
         */
        data class StepSolved(val stepId: Long) : Message

        /**
         * Represents a message, when stage is completed
         *
         * @property title Title of stage completed modal
         * @property stageCompletionGemsReward Gems reward for stage completion
         *
         * @see Action.ViewAction.ShowStageCompletedModal
         */
        data class StageCompleted(
            val title: String,
            val stageCompletionGemsReward: Int
        ) : Message

        /**
         * Represents a stage completed modal clicked "Go to home screen" event message.
         *
         * @see StageCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent
         * @see Action.ViewAction.NavigateTo.StudyPlan
         */
        object StageCompletedModalGoToStudyPlanClicked : Message

        /**
         * Represents a message, when project is completed
         *
         * @property stageCompletionGemsReward Gems reward for stage completion
         * @property projectCompletionGemsReward Gems reward for project completion
         *
         * @see Action.ViewAction.ShowProjectCompletedModal
         */
        data class ProjectCompleted(
            val stageCompletionGemsReward: Int,
            val projectCompletionGemsReward: Int
        ) : Message

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

    sealed interface Action {
        /**
         * Represents an action, when feature is initializing. Initiates data loading.
         *
         * @property projectId Project id to load
         * @property stageId Stage id to load
         *
         * @see Message.Initialize
         * @see Message.FetchStageImplementResult
         */
        data class FetchStageImplement(val projectId: Long, val stageId: Long) : Action

        /**
         * Represents an action, that checks stage completion
         *
         * @property stage Current stage
         *
         * @see Message.StageCompleted
         * @see Message.ProjectCompleted
         */
        data class CheckStageCompletionStatus(val stage: Stage) : Action

        /**
         * Represents an analytic action, logs analytic event to the analytic service.
         *
         * @property analyticEvent Analytic event to be logged
         */
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            /**
             * Represents a view action to show stage completion modal
             *
             * @property title Title of stage completed modal
             * @property stageCompletionGemsReward Gems reward for stage completion
             *
             * @see Message.StageCompleted
             */
            data class ShowStageCompletedModal(
                val title: String,
                val stageCompletionGemsReward: Int
            ) : ViewAction

            /**
             * Represents a view action to show project completion modal
             *
             * @property stageCompletionGemsReward Gems reward for stage completion
             * @property projectCompletionGemsReward Gems reward for project completion
             *
             * @see Message.ProjectCompleted
             */
            data class ShowProjectCompletedModal(
                val stageCompletionGemsReward: Int,
                val projectCompletionGemsReward: Int
            ) : ViewAction

            sealed interface NavigateTo : ViewAction {
                object StudyPlan : NavigateTo
            }
        }
    }
}
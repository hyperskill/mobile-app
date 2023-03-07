package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementClickedProjectDeprecatedButtonHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementUnsupportedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementUnsupportedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.stage_implement.domain.analytic.StageImplementViewedHyperskillAnalyticEvent
import org.hyperskill.app.stages.domain.model.Stage
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute

object StageImplementFeature {
    /**
     * Represents an internal feature state.
     */
    internal sealed interface State {
        /**
         * Represents an idle state.
         */
        object Idle : State

        /**
         * Represents a loading state.
         */
        object Loading : State

        /**
         * Represents a deprecated project state.
         */
        data class Deprecated(val project: Project) : State

        /**
         * Represents an unsupported state, when step solve is unsupported.
         * @see Action.ViewAction.ShowUnsupportedModal
         */
        object Unsupported : State

        /**
         * Represents a network error state.
         */
        object NetworkError : State

        /**
         * Represents a content state, when all necessary data is successfully loaded.
         *
         * @property project Current stage implementation project
         * @property stage Current stage implementation stage
         * @property step Current stage implementation step
         */
        data class Content(
            val project: Project,
            val stage: Stage,
            val step: Step
        ) : State
    }

    /**
     * Represents a public feature state, what is used for UI.
     */
    sealed interface ViewState {
        /**
         * Represents an idle state.
         */
        object Idle : ViewState

        /**
         * Represents a loading state.
         */
        object Loading : ViewState

        /**
         * Represents a deprecated project state.
         * We should display a placeholder with a message and a button (similar to the network error state).
         *
         * @property message Message to be displayed
         * @property ctaButtonText Text of the button
         *
         * @see Message.ProjectDeprecatedButtonClicked
         */
        data class Deprecated(val message: String, val ctaButtonText: String) : ViewState

        /**
         * Represents an unsupported state, when step solve is unsupported.
         * When feature state is unsupported, we should display an unsupported bottom sheet.
         *
         * @see Action.ViewAction.ShowUnsupportedModal
         */
        object Unsupported : ViewState

        /**
         * Represents a network error state.
         * We should display a network error placeholder.
         */
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
             * Represents a deprecated project result.
             *
             * @property project Deprecated project
             *
             * @see State.Deprecated
             */
            data class Deprecated(val project: Project) : FetchStageImplementResult

            /**
             * Represents an unsupported result, when step solve is unsupported.
             *
             * @see State.Unsupported
             */
            object Unsupported : FetchStageImplementResult

            /**
             * Represents a network error result.
             *
             * @see State.NetworkError
             */
            object NetworkError : FetchStageImplementResult

            /**
             * Represents a success result, when all necessary data is successfully loaded.
             *
             * @property project Current stage implementation project
             * @property stage Current stage implementation stage
             * @property step Current stage implementation step
             *
             * @see State.Content
             */
            data class Success(
                val project: Project,
                val stage: Stage,
                val step: Step
            ) : FetchStageImplementResult
        }

        /**
         * Represents a message, when user clicks on the deprecated project button.
         *
         * @see ViewState.Deprecated
         * @see Action.ViewAction.NavigateTo.Back
         * @see StageImplementClickedProjectDeprecatedButtonHyperskillAnalyticEvent
         */
        object ProjectDeprecatedButtonClicked : Message

        /**
         * Unsupported modal
         */

        /**
         * Represents a shown analytic event message for unsupported modal.
         *
         * @see StageImplementUnsupportedModalShownHyperskillAnalyticEvent
         */
        object UnsupportedModalShownEventMessage : Message

        /**
         * Represents a hidden analytic event message for unsupported modal.
         *
         * @see StageImplementUnsupportedModalHiddenHyperskillAnalyticEvent
         */
        object UnsupportedModalHiddenEventMessage : Message

        /**
         * Represents a message, when user clicks on the unsupported modal go to home screen button.
         *
         * @see Action.ViewAction.NavigateTo.HomeScreen
         * @see StageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent
         */
        object UnsupportedModalGoToHomeScreenClicked : Message

        /**
         * Represents a feature view analytic event message.
         *
         * @see StageImplementViewedHyperskillAnalyticEvent
         */
        object ViewedEventMessage : Message
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
         * Represents an analytic action, logs analytic event to the analytic service.
         *
         * @property analyticEvent Analytic event to be logged
         */
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            /**
             * Represents a view action that displays unsupported modal.
             *
             * @see State.Unsupported
             * @see Message.FetchStageImplementResult.Unsupported
             */
            object ShowUnsupportedModal : ViewAction

            sealed interface NavigateTo : ViewAction {
                /**
                 * Represents a view action that navigates to the previous screen.
                 *
                 * @see Message.ProjectDeprecatedButtonClicked
                 */
                object Back : NavigateTo

                /**
                 * Represents a view action that navigates to the home screen.
                 *
                 * @see Message.UnsupportedModalGoToHomeScreenClicked
                 */
                object HomeScreen : NavigateTo
            }
        }
    }
}
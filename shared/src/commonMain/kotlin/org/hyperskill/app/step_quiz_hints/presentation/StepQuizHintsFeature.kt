package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.CommentThread
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.step.domain.model.Step

object StepQuizHintsFeature {
    fun isHintsFeatureAvailable(step: Step): Boolean =
        step.commentsStatistics.any { it.thread == CommentThread.HINT && it.totalCount > 0 }

    sealed interface State {
        data object Idle : State

        /**
         * @property isInitialLoading true in case of initial data loading, false in case of certain hint loading
         * */
        data class Loading(val isInitialLoading: Boolean = false) : State

        /**
         * State of content displaying
         *
         * @property hintsIds remaining hints to be displayed
         * @property currentHint current hint to be displayed
         * @property hintHasReaction flag true, if user created reaction or reported hint
         * @property areHintsLimited used for showing only one hint for some subscriptions
         * @property stepId used for analytic route
         */
        data class Content(
            val hintsIds: List<Long>,
            val currentHint: Comment?,
            val hintHasReaction: Boolean,
            val areHintsLimited: Boolean,
            val stepId: Long
        ) : State

        /**
         * State of network error
         *
         * @property nextHintId next hint to be loaded
         * @property hintsIds remaining hints to be displayed
         * @property areHintsLimited used for showing only one hint for some subscriptions
         * @property stepId used for analytic route
         */
        data class NetworkError(
            val nextHintId: Long,
            val hintsIds: List<Long>,
            val areHintsLimited: Boolean,
            val stepId: Long
        ) : State
    }

    sealed interface ViewState {
        data object Idle : ViewState

        data object InitialLoading : ViewState

        data object HintLoading : ViewState

        sealed interface Content : ViewState {
            object SeeHintButton : Content
            data class HintCard(
                val authorAvatar: String,
                val authorName: String,
                val hintText: String,
                val hintState: HintState
            ) : Content
        }

        data object Error : ViewState

        enum class HintState {
            REACT_TO_HINT,
            SEE_NEXT_HINT,
            LAST_HINT
        }
    }

    sealed interface Message {
        /**
         * Trigger of state initialization
         *
         * @property stepId step id to load hints for it
         */
        data class InitWithStepId(val stepId: Long) : Message

        /**
         * Message to fill state with ready data
         *
         * @property hintsIds  hints ids to be displayed
         * @property areHintsLimited used for showing only one hint for some subscriptions
         * @property stepId used for analytic route
         */
        data class HintsIdsLoaded(
            val hintsIds: List<Long>,
            val lastSeenHint: Comment?,
            val lastSeenHintHasReaction: Boolean,
            val areHintsLimited: Boolean,
            val stepId: Long
        ) : Message

        /**
         * Message to create user reaction for current hint
         *
         * @property reaction reaction that user chose
         */
        data class ReactionButtonClicked(val reaction: ReactionType) : Message

        /**
         * Indicates that react hint succeed
         */
        data object ReactHintSuccess : Message

        /**
         * Indicates that react hint failed
         */
        data object ReactHintFailure : Message

        /**
         * Creates report for current hint, assumes that user confirmed that action
         */
        data object ReportHint : Message

        /**
         * Indicates that report hint succeed
         */
        data object ReportHintSuccess : Message

        /**
         * Indicates that report hint failed
         */
        data object ReportHintFailure : Message

        /**
         * Initiate loading next hint
         */
        data object LoadHintButtonClicked : Message

        /**
         * Message to fill state with ready data
         *
         * @property nextHint new loaded hint
         * @property remainingHintsIds next hints ids to be displayed
         * @property areHintsLimited used for showing only one hint for some subscriptions
         * @property stepId used for analytic route
         */
        data class NextHintLoaded(
            val nextHint: Comment,
            val remainingHintsIds: List<Long>,
            val areHintsLimited: Boolean,
            val stepId: Long
        ) : Message

        /**
         * Message to change state to NetworkError and save necessary data
         *
         * @property nextHintId next hint to be loaded
         * @property remainingHintsIds remaining hints ids
         * @property areHintsLimited used for showing only one hint for some subscriptions
         * @property stepId used for analytic route
         */
        data class NextHintLoadingError(
            val nextHintId: Long,
            val remainingHintsIds: List<Long>,
            val areHintsLimited: Boolean,
            val stepId: Long
        ) : Message

        /**
         * Analytic
         */
        data object ClickedReportEventMessage : Message
        data object ReportHintNoticeShownEventMessage : Message
        data class ReportHintNoticeHiddenEventMessage(val isReported: Boolean) : Message
    }

    internal sealed interface InternalMessage : Message {
        /**
         * Initiate loading next hint without analytic event
         */
        data object InitiateHintLoading : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            /**
             * Shows snackbar with error message
             */
            data object ShowNetworkError : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        /**
         * Reporting hint action
         *
         * @property hintId hint ID to be reported
         * @property stepId is used to create user storage record
         */
        data class ReportHint(val hintId: Long, val stepId: Long) : InternalAction

        /**
         * Creating reaction for hint
         *
         * @property hintId hint ID to create reaction for it
         * @property stepId is used to create user storage record
         * @property reaction reaction from user
         */
        data class ReactHint(val hintId: Long, val stepId: Long, val reaction: ReactionType) : InternalAction

        /**
         * Loading all hints IDs for given step
         *
         * @property stepId step ID to load hints for it
         */
        data class FetchHintsIds(val stepId: Long) : InternalAction

        /**
         * Loading next hint action
         *
         * @property nextHintId hint ID to load hint details
         * @property remainingHintsIds next hints ids to be displayed
         * @property areHintsLimited used for showing only one hint for some subscriptions
         * @property stepId used for analytic route
         */
        data class FetchNextHint(
            val nextHintId: Long,
            val remainingHintsIds: List<Long>,
            val areHintsLimited: Boolean,
            val stepId: Long
        ) : InternalAction

        /**
         * Logging analytic event action
         *
         * @property analyticEvent event to be logged
         */
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}
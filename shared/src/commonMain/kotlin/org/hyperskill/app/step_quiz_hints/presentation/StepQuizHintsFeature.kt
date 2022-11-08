package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.ReactionType

interface StepQuizHintsFeature {
    sealed interface State {
        object Idle : State
        object Loading : State

        data class Content(
            val hintsIds: List<Long>,
            val currentHint: Comment?,
            val hintHasReaction: Boolean,
            val isDailyStep: Boolean,
            val stepId: Long
        ) : State

        data class NetworkError(
            val nextHintId: Long,
            val hintsIds: List<Long>,
            val isDailyStep: Boolean,
            val stepId: Long
        ) : State
    }

    sealed interface Message {
        data class InitWithStepId(val stepId: Long) : Message
        data class HintsIdsLoaded(
            val hintsIds: List<Long>,
            val isDailyStep: Boolean,
            val stepId: Long
        ) : Message

        data class ReactionButtonClicked(
            val reaction: ReactionType
        ) : Message
        object HintReported : Message

        object LoadHintButtonClicked : Message
        data class NextHintLoaded(
            val nextHint: Comment,
            val remainingHintsIds: List<Long>,
            val isDailyStep: Boolean,
            val stepId: Long
        ) : Message
        data class NextHintLoadingError(
            val nextHintId: Long,
            val remainingHintsIds: List<Long>,
            val isDailyStep: Boolean,
            val stepId: Long
        ) : Message

        /**
         * Analytic
         */
        object ClickedReportEventMessage : Message
        object ReportHintNoticeShownEventMessage : Message
        data class ReportHintNoticeHiddenEventMessage(val isReported: Boolean) : Message
    }

    sealed interface Action {
        data class ReportHint(val hintId: Long, val stepId: Long) : Action
        data class ReactHint(val hintId: Long, val stepId: Long, val reaction: ReactionType) : Action

        data class FetchHintsIds(val stepId: Long) : Action

        data class FetchNextHint(
            val nextHintId: Long,
            val remainingHintsIds: List<Long>,
            val isDailyStep: Boolean,
            val stepId: Long
        ) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed class ViewAction : Action
    }
}
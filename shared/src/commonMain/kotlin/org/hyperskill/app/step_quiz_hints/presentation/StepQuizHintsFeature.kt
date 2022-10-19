package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.ReactionType

interface StepQuizHintsFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(
            val hintsIds: List<Long>,
            val currentHint: Comment?,
            val hintHasReaction: Boolean
        ) : State

        object NetworkError : State
    }

    sealed interface Message {
        data class InitWithHintsIDs(val hintsIds: List<Long>) : Message

        data class ReactionButtonClicked(
            val reaction: ReactionType
        ) : Message
        object HintReported : Message

        object NextHintButtonClicked : Message
        data class NextHintLoaded(
            val nextHint: Comment,
            val remainingHintsIds: List<Long>
        ) : Message

        /**
         * Analytic
         */
        // TODO: decide what should we send to analytic
    }

    sealed interface Action {
        data class ReportHint(val hintId: Long, val stepId: Long) : Action
        data class ReactHint(val hintId: Long, val stepId: Long, val reaction: ReactionType) : Action

        data class FetchNextHint(
            val nextHintId: Long,
            val remainingHintsIds: List<Long>
        ) : Action

        sealed interface ViewAction : Action

        /**
         * Analytic
         */
        // TODO: decide what should we send to analytic
    }
}
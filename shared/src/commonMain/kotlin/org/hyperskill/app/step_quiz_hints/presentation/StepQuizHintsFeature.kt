package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.ReactionType

interface StepQuizHintsFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(
            val hintsIDs: List<Long>,
            val currentHint: Comment?,
            val hintHasReaction: Boolean
        ) : State

        object NetworkError : State
    }

    sealed interface Message {
        data class InitWithHintsIDs(val hintsIDs: List<Long>) : Message

        data class ReactionButtonClicked(
            val reaction: ReactionType
        ) : Message
        object HintReported : Message

        object NextHintButtonClicked : Message
        data class NextHintLoaded(
            val nextHint: Comment,
            val remainingHintsIDs: List<Long>
        ) : Message

        /**
         * Analytic
         */
        // TODO: decide what should we send to analytic
    }

    sealed interface Action {
        data class ReportHint(val hintID: Long, val stepID: Long) : Action
        data class ReactHint(val hintID: Long, val stepID: Long, val reaction: ReactionType) : Action

        data class FetchNextHint(
            val nextHintID: Long,
            val remainingHintsIDs: List<Long>
        ) : Action

        sealed interface ViewAction : Action

        /**
         * Analytic
         */
        // TODO: decide what should we send to analytic
    }
}
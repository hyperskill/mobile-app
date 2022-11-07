package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.comments.domain.model.ReactionType
import org.hyperskill.app.step_quiz_hints.domain.analytic.StepQuizHintsClickedHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_hints.domain.analytic.StepQuizHintsHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_hints.domain.analytic.StepQuizHintsShownHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepQuizHintsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.InitWithStepId ->
                if (state is State.Idle) {
                    State.Loading to setOf(Action.FetchHintsIds(message.stepId))
                } else {
                    null
                }
            is Message.HintsIdsLoaded ->
                if (state is State.Loading) {
                    State.Content(message.hintsIds, null, false, message.dailyStepId) to emptySet()
                } else {
                    null
                }
            is Message.HintReported ->
                if (state is State.Content && state.currentHint != null) {
                    state.copy(hintHasReaction = true) to setOf(
                        Action.ReportHint(
                            hintId = state.currentHint.id,
                            stepId = state.currentHint.targetId
                        ),
                        Action.LogAnalyticEvent(
                            StepQuizHintsHiddenHyperskillAnalyticEvent(
                                resolveAnalyticRoute(state.currentHint.targetId, state.dailyStepId),
                                isReported = true
                            )
                        )
                    )
                } else {
                    null
                }
            is Message.ReactionButtonClicked ->
                if (state is State.Content && state.currentHint != null) {
                    state.copy(hintHasReaction = true) to setOf(
                        Action.ReactHint(
                            hintId = state.currentHint.id,
                            stepId = state.currentHint.targetId,
                            reaction = message.reaction
                        ),
                        Action.LogAnalyticEvent(
                            StepQuizHintsClickedHyperskillAnalyticEvent(
                                resolveAnalyticRoute(state.currentHint.targetId, state.dailyStepId),
                                when (message.reaction) {
                                    ReactionType.HELPFUL -> HyperskillAnalyticTarget.YES
                                    ReactionType.UNHELPFUL -> HyperskillAnalyticTarget.NO
                                },
                                commentId = state.currentHint.id
                            )
                        )
                    )
                } else {
                    null
                }
            is Message.LoadHintButtonClicked ->
                when (state) {
                    is State.Content -> {
                        val hintsIds = state.hintsIds.toMutableList()
                        val nextHintId = hintsIds.removeLast()
                        State.Loading to setOf(Action.FetchNextHint(nextHintId, hintsIds, state.dailyStepId))
                    }
                    is State.NetworkError -> {
                        State.Loading to setOf(
                            Action.FetchNextHint(
                                state.nextHintId,
                                state.hintsIds,
                                state.dailyStepId
                            )
                        )
                    }
                    else -> {
                        null
                    }
                }
            is Message.NextHintLoaded ->
                if (state is State.Loading) {
                    State.Content(message.remainingHintsIds, message.nextHint, false, message.dailyStepId) to emptySet()
                } else {
                    null
                }
            is Message.NextHintLoadingError ->
                if (state is State.Loading) {
                    State.NetworkError(message.nextHintId, message.remainingHintsIds, message.dailyStepId) to emptySet()
                } else {
                    null
                }
            is Message.ReportHintNoticeHiddenEventMessage ->
                if (state is State.Content && state.currentHint != null) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            StepQuizHintsHiddenHyperskillAnalyticEvent(
                                resolveAnalyticRoute(state.currentHint.targetId, state.dailyStepId),
                                isReported = false
                            )
                        )
                    )
                } else {
                    null
                }
            is Message.ReportSeeHintClickedEventMessage ->
                if (state is State.Content && state.currentHint != null) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            StepQuizHintsClickedHyperskillAnalyticEvent(
                                resolveAnalyticRoute(state.currentHint.targetId, state.dailyStepId),
                                HyperskillAnalyticTarget.SEE_HINT
                            )
                        )
                    )
                } else {
                    null
                }
            is Message.ReportHintReportClickedEventMessage ->
                if (state is State.Content && state.currentHint != null) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            StepQuizHintsClickedHyperskillAnalyticEvent(
                                resolveAnalyticRoute(state.currentHint.targetId, state.dailyStepId),
                                HyperskillAnalyticTarget.REPORT,
                                commentId = state.currentHint.id
                            )
                        )
                    )
                } else {
                    null
                }
            is Message.ReportHintReportShownEventMessage ->
                if (state is State.Content && state.currentHint != null) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            StepQuizHintsShownHyperskillAnalyticEvent(
                                resolveAnalyticRoute(state.currentHint.targetId, state.dailyStepId)
                            )
                        )
                    )
                } else {
                    null
                }
        } ?: (state to emptySet())

    private fun resolveAnalyticRoute(currentStepId: Long, dailyStepId: Long?): HyperskillAnalyticRoute =
        if (currentStepId == dailyStepId)
            HyperskillAnalyticRoute.Learn.Daily(currentStepId)
        else HyperskillAnalyticRoute.Learn.Step(currentStepId)
}
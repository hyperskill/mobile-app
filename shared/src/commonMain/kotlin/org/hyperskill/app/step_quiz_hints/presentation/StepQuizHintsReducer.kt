package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.comments.domain.model.ReactionType
import org.hyperskill.app.step_quiz_hints.domain.analytic.StepQuizHintsClickedHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_hints.domain.analytic.StepQuizHintsHiddenReportHintNoticeHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_hints.domain.analytic.StepQuizHintsShownReportHintNoticeHyperskillAnalyticEvent
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
                    State.Content(
                        hintsIds = message.hintsIds,
                        currentHint = message.lastSeenHint,
                        hintHasReaction = false,
                        isDailyStep = message.isDailyStep,
                        stepId = message.stepId
                    ) to emptySet()
                } else {
                    null
                }
            is Message.ReportHint ->
                if (state is State.Content && state.currentHint != null) {
                    state to setOf(
                        Action.ReportHint(
                            hintId = state.currentHint.id,
                            stepId = state.currentHint.targetId
                        )
                    )
                } else {
                    null
                }
            is Message.ReportHintSuccess ->
                if (state is State.Content && state.currentHint != null) {
                    state.copy(hintHasReaction = true) to emptySet()
                } else {
                    null
                }
            is Message.ReportHintFailure ->
                if (state is State.Content) {
                    state to setOf(Action.ViewAction.ShowNetworkError)
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
                                resolveAnalyticRoute(state.currentHint.targetId, state.isDailyStep),
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
                        State.Loading to setOf(
                            Action.FetchNextHint(nextHintId, hintsIds, state.isDailyStep, state.stepId),
                            Action.LogAnalyticEvent(
                                StepQuizHintsClickedHyperskillAnalyticEvent(
                                    resolveAnalyticRoute(state.stepId, state.isDailyStep),
                                    when (state.currentHint) {
                                        null -> HyperskillAnalyticTarget.SEE_HINT
                                        else -> HyperskillAnalyticTarget.SEE_NEXT_HINT
                                    }
                                )
                            )
                        )
                    }
                    is State.NetworkError -> {
                        State.Loading to setOf(
                            Action.FetchNextHint(
                                nextHintId = state.nextHintId,
                                remainingHintsIds = state.hintsIds,
                                isDailyStep = state.isDailyStep,
                                stepId = state.stepId
                            )
                        )
                    }
                    else -> {
                        null
                    }
                }
            is Message.NextHintLoaded ->
                if (state is State.Loading) {
                    State.Content(
                        hintsIds = message.remainingHintsIds,
                        currentHint = message.nextHint,
                        hintHasReaction = false,
                        isDailyStep = message.isDailyStep,
                        stepId = message.stepId
                    ) to emptySet()
                } else {
                    null
                }
            is Message.NextHintLoadingError ->
                if (state is State.Loading) {
                    State.NetworkError(
                        nextHintId = message.nextHintId,
                        hintsIds = message.remainingHintsIds,
                        isDailyStep = message.isDailyStep,
                        stepId = message.stepId
                    ) to emptySet()
                } else {
                    null
                }
            is Message.ReportHintNoticeHiddenEventMessage ->
                if (state is State.Content && state.currentHint != null) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            StepQuizHintsHiddenReportHintNoticeHyperskillAnalyticEvent(
                                resolveAnalyticRoute(state.currentHint.targetId, state.isDailyStep),
                                isReported = message.isReported
                            )
                        )
                    )
                } else {
                    null
                }
            is Message.ClickedReportEventMessage ->
                if (state is State.Content && state.currentHint != null) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            StepQuizHintsClickedHyperskillAnalyticEvent(
                                resolveAnalyticRoute(state.stepId, state.isDailyStep),
                                HyperskillAnalyticTarget.REPORT,
                                commentId = state.currentHint.id
                            )
                        )
                    )
                } else {
                    null
                }
            is Message.ReportHintNoticeShownEventMessage ->
                if (state is State.Content) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            StepQuizHintsShownReportHintNoticeHyperskillAnalyticEvent(
                                resolveAnalyticRoute(state.stepId, state.isDailyStep)
                            )
                        )
                    )
                } else {
                    null
                }
        } ?: (state to emptySet())

    private fun resolveAnalyticRoute(stepId: Long, isDailyStep: Boolean): HyperskillAnalyticRoute =
        if (isDailyStep) HyperskillAnalyticRoute.Learn.Daily(stepId)
        else HyperskillAnalyticRoute.Learn.Step(stepId)
}
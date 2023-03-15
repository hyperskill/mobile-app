package org.hyperskill.app.step_quiz_hints.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_hints.domain.analytic.StepQuizHintsClickedHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_hints.domain.analytic.StepQuizHintsHiddenReportHintNoticeHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_hints.domain.analytic.StepQuizHintsShownReportHintNoticeHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Action
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.Message
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class StepQuizHintsReducer(private val stepRoute: StepRoute) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.InitWithStepId ->
                if (state is State.Idle) {
                    State.Loading(isInitialLoading = true) to setOf(Action.FetchHintsIds(message.stepId))
                } else {
                    null
                }
            is Message.HintsIdsLoaded ->
                if (state is State.Loading) {
                    State.Content(
                        hintsIds = message.hintsIds,
                        currentHint = message.lastSeenHint,
                        hintHasReaction = message.lastSeenHintHasReaction,
                        isFreemium = message.isFreemium,
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
                    state to setOf(
                        Action.ReactHint(
                            hintId = state.currentHint.id,
                            stepId = state.currentHint.targetId,
                            reaction = message.reaction
                        ),
                        Action.LogAnalyticEvent(
                            StepQuizHintsClickedHyperskillAnalyticEvent(
                                stepRoute.analyticRoute,
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
            is Message.ReactHintSuccess ->
                if (state is State.Content) {
                    state.copy(hintHasReaction = true) to emptySet()
                } else {
                    null
                }
            is Message.ReactHintFailure ->
                if (state is State.Content) {
                    state to setOf(Action.ViewAction.ShowNetworkError)
                } else {
                    null
                }
            is Message.LoadHintButtonClicked ->
                when (state) {
                    is State.Content -> {
                        val hintsIds = state.hintsIds.toMutableList()
                        val nextHintId = hintsIds.removeLast()
                        State.Loading() to setOf(
                            Action.FetchNextHint(
                                nextHintId,
                                hintsIds,
                                state.isFreemium,
                                state.stepId
                            ),
                            Action.LogAnalyticEvent(
                                StepQuizHintsClickedHyperskillAnalyticEvent(
                                    stepRoute.analyticRoute,
                                    when (state.currentHint) {
                                        null -> HyperskillAnalyticTarget.SEE_HINT
                                        else -> HyperskillAnalyticTarget.SEE_NEXT_HINT
                                    }
                                )
                            )
                        )
                    }
                    is State.NetworkError -> {
                        State.Loading() to setOf(
                            Action.FetchNextHint(
                                nextHintId = state.nextHintId,
                                remainingHintsIds = state.hintsIds,
                                isFreemium = state.isFreemium,
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
                        isFreemium = message.isFreemium,
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
                        isFreemium = message.isFreemium,
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
                                stepRoute.analyticRoute,
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
                                stepRoute.analyticRoute,
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
                            StepQuizHintsShownReportHintNoticeHyperskillAnalyticEvent(stepRoute.analyticRoute)
                        )
                    )
                } else {
                    null
                }
        } ?: (state to emptySet())
}
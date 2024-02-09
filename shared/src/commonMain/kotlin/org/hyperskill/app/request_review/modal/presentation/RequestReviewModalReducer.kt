package org.hyperskill.app.request_review.modal.presentation

import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.request_review.modal.domain.analytic.RequestReviewModalClickHyperskillAnalyticEvent
import org.hyperskill.app.request_review.modal.domain.analytic.RequestReviewModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.request_review.modal.domain.analytic.RequestReviewModalShownHyperskillAnalyticEvent
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.Action
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.InternalAction
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.Message
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.State
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class RequestReviewModalReducer(
    private val stepRoute: StepRoute,
    private val resourceProvider: ResourceProvider
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            Message.PositiveButtonClicked ->
                handlePositiveButtonClickedMessage(state)
            Message.NegativeButtonClicked ->
                handleNegativeButtonClickedMessage(state)
            // Analytic
            Message.ShownEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        RequestReviewModalShownHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            Message.HiddenEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        RequestReviewModalHiddenHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
        } ?: (state to emptySet())

    private fun handlePositiveButtonClickedMessage(
        state: State
    ): ReducerResult? =
        when (state) {
            State.Awaiting ->
                State.Positive to setOf(
                    InternalAction.LogAnalyticEvent(
                        RequestReviewModalClickHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            target = HyperskillAnalyticTarget.YES
                        )
                    ),
                    Action.ViewAction.RequestUserReview
                )
            State.Negative ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        RequestReviewModalClickHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            target = HyperskillAnalyticTarget.WRITE_A_REQUEST
                        )
                    ),
                    Action.ViewAction.SubmitSupportRequest(
                        resourceProvider.getString(SharedResources.strings.settings_report_problem_url)
                    )
                )
            State.Positive -> null
        }

    private fun handleNegativeButtonClickedMessage(
        state: State
    ): ReducerResult? =
        when (state) {
            State.Awaiting ->
                State.Negative to setOf(
                    InternalAction.LogAnalyticEvent(
                        RequestReviewModalClickHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            target = HyperskillAnalyticTarget.NO
                        )
                    )
                )
            State.Negative ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        RequestReviewModalClickHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            target = HyperskillAnalyticTarget.MAYBE_LATER
                        )
                    ),
                    Action.ViewAction.Dismiss
                )
            State.Positive -> null
        }
}
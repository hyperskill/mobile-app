package org.hyperskill.app.problems_limit_reached.presentation

import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Action
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.InternalAction
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Message
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.State
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.problems_limit_reached.domain.analytic.ProblemsLimitReachedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.problems_limit_reached.domain.analytic.ProblemsLimitReachedModalClickedUnlockUnlimitedProblemsHSAnalyticEvent
import org.hyperskill.app.problems_limit_reached.domain.analytic.ProblemsLimitReachedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.problems_limit_reached.domain.analytic.ProblemsLimitReachedModalShownHyperskillAnalyticEvent
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ProblemsLimitReachedReducerResult = Pair<State, Set<Action>>

internal class ProblemsLimitReachedModalReducer(
    private val stepRoute: StepRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ProblemsLimitReachedReducerResult =
        when (message) {
            Message.ShownEventMessage -> handleModalShown(state)
            Message.HiddenEventMessage -> handleModalHidden(state)
            Message.GoToHomeScreenClicked -> handleGoHomeClicked(state)
            Message.UnlockUnlimitedProblemsClicked -> handleUnlockUnlimitedProblemsClicked(state)
        }

    private fun handleModalShown(state: State): ProblemsLimitReachedReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                ProblemsLimitReachedModalShownHyperskillAnalyticEvent(stepRoute.analyticRoute)
            )
        )

    private fun handleModalHidden(state: State): ProblemsLimitReachedReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                ProblemsLimitReachedModalHiddenHyperskillAnalyticEvent(stepRoute.analyticRoute)
            )
        )

    private fun handleGoHomeClicked(state: State): ProblemsLimitReachedReducerResult =
        state to setOf(
            Action.ViewAction.NavigateTo.Home,
            InternalAction.LogAnalyticEvent(
                ProblemsLimitReachedModalClickedGoToHomeScreenHyperskillAnalyticEvent(stepRoute.analyticRoute)
            )
        )

    private fun handleUnlockUnlimitedProblemsClicked(state: State): ProblemsLimitReachedReducerResult =
        state to setOf(
            Action.ViewAction.NavigateTo.Paywall(PaywallTransitionSource.PROBLEMS_LIMIT_MODAL),
            InternalAction.LogAnalyticEvent(
                ProblemsLimitReachedModalClickedUnlockUnlimitedProblemsHSAnalyticEvent(stepRoute.analyticRoute)
            )
        )
}
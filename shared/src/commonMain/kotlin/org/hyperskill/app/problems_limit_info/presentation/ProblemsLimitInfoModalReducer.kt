package org.hyperskill.app.problems_limit_info.presentation

import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.problems_limit_info.domain.analytic.ProblemsLimitInfoModalClickedUnlockUnlimitedProblemsHSAnalyticEvent
import org.hyperskill.app.problems_limit_info.domain.analytic.ProblemsLimitInfoModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.problems_limit_info.domain.analytic.ProblemsLimitInfoModalShownHyperskillAnalyticEvent
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.Action
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.InternalAction
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.Message
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.State
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ProblemsLimitInfoReducerResult = Pair<State, Set<Action>>

internal class ProblemsLimitInfoModalReducer(
    private val stepRoute: StepRoute
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ProblemsLimitInfoReducerResult =
        when (message) {
            Message.ShownEventMessage -> handleModalShown(state)
            Message.HiddenEventMessage -> handleModalHidden(state)
            Message.UnlockUnlimitedProblemsClicked -> handleUnlockUnlimitedProblemsClicked(state)
        }

    private fun handleModalShown(state: State): ProblemsLimitInfoReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                ProblemsLimitInfoModalShownHyperskillAnalyticEvent(stepRoute.analyticRoute)
            )
        )

    private fun handleModalHidden(state: State): ProblemsLimitInfoReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                ProblemsLimitInfoModalHiddenHyperskillAnalyticEvent(stepRoute.analyticRoute)
            )
        )

    private fun handleUnlockUnlimitedProblemsClicked(state: State): ProblemsLimitInfoReducerResult =
        state to setOf(
            Action.ViewAction.NavigateTo.Paywall(PaywallTransitionSource.PROBLEMS_LIMIT_MODAL),
            InternalAction.LogAnalyticEvent(
                ProblemsLimitInfoModalClickedUnlockUnlimitedProblemsHSAnalyticEvent(stepRoute.analyticRoute)
            )
        )
}
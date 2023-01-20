package org.hyperskill.app.step.presentation

import org.hyperskill.app.step.domain.analytic.StepViewedHyperskillAnalyticEvent
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step.presentation.StepFeature.PracticeStatus
import org.hyperskill.app.step.presentation.StepFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Data || state is State.Error))
                ) {
                    State.Loading to setOf(Action.FetchStep(message.stepRoute))
                } else {
                    null
                }
            is Message.StepLoaded.Success ->
                State.Data(message.step, message.practiceStatus) to emptySet()
            is Message.StepLoaded.Error ->
                State.Error to emptySet()
            is Message.ViewedEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepViewedHyperskillAnalyticEvent(
                            message.stepRoute.analyticRoute
                        )
                    )
                )
            is Message.StartPracticingClicked ->
                if (state is State.Data) {
                    state.copy(practiceStatus = PracticeStatus.LOADING) to setOf(
                        Action.FetchNextStepQuiz(state.step)
                    )
                } else {
                    null
                }
            is Message.NextStepQuizFetchedStatus.Success ->
                if (state is State.Data) {
                    state to setOf(Action.ViewAction.ReloadStep(message.stepRoute))
                } else {
                    null
                }

            is Message.NextStepQuizFetchedStatus.Error ->
                if (state is State.Data) {
                    state.copy(practiceStatus = PracticeStatus.AVAILABLE) to setOf(
                        Action.ViewAction.ShowStartPracticingErrorStatus
                    )
                } else {
                    null
                }
        } ?: (state to emptySet())
}
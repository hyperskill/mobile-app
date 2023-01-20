package org.hyperskill.app.step.presentation

import org.hyperskill.app.step.domain.analytic.StepClickedStartPracticingHyperskillAnalyticEvent
import org.hyperskill.app.step.domain.analytic.StepViewedHyperskillAnalyticEvent
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step.presentation.StepFeature.PracticeStatus
import org.hyperskill.app.step.presentation.StepFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepReducer(private val stepRoute: StepRoute) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Data || state is State.Error))
                ) {
                    State.Loading to setOf(Action.FetchStep(stepRoute))
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
                            stepRoute.analyticRoute
                        )
                    )
                )
            is Message.StartPracticingClicked ->
                if (state is State.Data) {
                    state.copy(practiceStatus = PracticeStatus.LOADING) to setOf(
                        Action.FetchNextStepQuiz(state.step),
                        Action.LogAnalyticEvent(
                            StepClickedStartPracticingHyperskillAnalyticEvent(stepRoute.analyticRoute)
                        )
                    )
                } else {
                    null
                }
            is Message.NextStepQuizFetchedStatus.Success ->
                if (state is State.Data) {
                    state to setOf(Action.ViewAction.ReloadStep(message.newStepRoute))
                } else {
                    null
                }

            is Message.NextStepQuizFetchedStatus.Error ->
                if (state is State.Data) {
                    state.copy(practiceStatus = PracticeStatus.AVAILABLE) to setOf(
                        Action.ViewAction.ShowPracticingErrorStatus(message.errorMessage)
                    )
                } else {
                    null
                }
        } ?: (state to emptySet())
}
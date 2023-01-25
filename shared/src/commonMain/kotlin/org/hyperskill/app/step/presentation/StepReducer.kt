package org.hyperskill.app.step.presentation

import org.hyperskill.app.step.domain.analytic.StepViewedHyperskillAnalyticEvent
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step.presentation.StepFeature.State
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_completion.presentation.StepCompletionReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepReducer(private val stepRoute: StepRoute, private val stepCompletionReducer: StepCompletionReducer) : StateReducer<State, Message, Action> {
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
            is Message.StepLoaded.Success -> {
                State.Data(
                    step = message.step,
                    isPracticingAvailable = stepRoute is StepRoute.Learn,
                    stepCompletionState = StepCompletionFeature.State(
                        currentStep = message.step,
                        continueButtonAction = if (stepRoute is StepRoute.Learn) {
                            StepCompletionFeature.ContinueButtonAction.FetchNextStepQuiz
                        } else {
                            StepCompletionFeature.ContinueButtonAction.NavigateToBack
                        }
                    )
                ) to emptySet()
            }
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
            is Message.StepCompletionMessage ->
                if (state is State.Data) {
                    val (stepCompletionState, stepCompletionActions) =
                        reduceStepCompletionMessage(state.stepCompletionState, message.message)
                    state.copy(stepCompletionState = stepCompletionState) to stepCompletionActions
                } else {
                    null
                }
        } ?: (state to emptySet())

    /**
     * Reduces [Message.StepCompletionMessage] to [StepCompletionFeature.State] and set of [StepFeature.Action]
     */
    private fun reduceStepCompletionMessage(
        state: StepCompletionFeature.State,
        message: StepCompletionFeature.Message
    ): Pair<StepCompletionFeature.State, Set<Action>> {
        val (gamificationToolbarState, gamificationToolbarActions) = stepCompletionReducer.reduce(state, message)

        val actions = gamificationToolbarActions
            .map {
                if (it is StepCompletionFeature.Action.ViewAction) {
                    Action.ViewAction.StepCompletionViewAction(it)
                } else {
                    Action.StepCompletionAction(it)
                }
            }
            .toSet()

        return gamificationToolbarState to actions
    }
}
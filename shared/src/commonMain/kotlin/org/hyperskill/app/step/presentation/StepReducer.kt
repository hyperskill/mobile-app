package org.hyperskill.app.step.presentation

import org.hyperskill.app.step.domain.analytic.StepViewedHyperskillAnalyticEvent
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.InternalAction
import org.hyperskill.app.step.presentation.StepFeature.InternalMessage
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step.presentation.StepFeature.State
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_completion.presentation.StepCompletionReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class StepReducer(
    private val stepRoute: StepRoute,
    private val stepCompletionReducer: StepCompletionReducer
) : StateReducer<State, Message, Action> {

    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Data || state is State.Error))
                ) {
                    State.Loading to setOf(
                        InternalAction.FetchStep(stepRoute),
                        InternalAction.ViewStep(stepRoute.stepId, stepRoute.stepContext)
                    )
                } else {
                    null
                }
            is Message.StepLoaded.Success ->
                handleStepLoadedSuccess(message)
            is Message.StepLoaded.Error ->
                State.Error to emptySet()
            is Message.ScreenShowed ->
                handleScreenShowed(state)
            is Message.ScreenHidden ->
                handleScreenHidden(state)
            is InternalMessage.SolvingTimerFired ->
                handleSolvingTimerFired(state)
            is InternalMessage.StepCompleted ->
                handleStepCompleted(state, message)
            is Message.StepCompletionMessage ->
                if (state is State.Data) {
                    val (stepCompletionState, stepCompletionActions) =
                        reduceStepCompletionMessage(state.stepCompletionState, message.message)
                    state.copy(stepCompletionState = stepCompletionState) to stepCompletionActions
                } else {
                    null
                }
        } ?: (state to emptySet())

    private fun handleStepLoadedSuccess(message: Message.StepLoaded.Success): ReducerResult {
        val newState = State.Data(
            step = message.step,
            isPracticingAvailable = isPracticingAvailable(stepRoute),
            stepCompletionState = StepCompletionFeature.createState(message.step, stepRoute)
        )
        return newState to buildSet {
            add(InternalAction.UpdateNextLearningActivityState(message.step))
            if (shouldLogStepSolvingTime(newState)) {
                add(InternalAction.StartSolvingTimer)
            }
        }
    }

    private fun isPracticingAvailable(stepRoute: StepRoute) =
        when (stepRoute) {
            is StepRoute.Learn.Step,
            is StepRoute.Learn.TheoryOpenedFromPractice ->
                true
            is StepRoute.Learn.TheoryOpenedFromSearch,
            is StepRoute.LearnDaily,
            is StepRoute.Repeat.Practice,
            is StepRoute.Repeat.Theory,
            is StepRoute.StageImplement,
            is StepRoute.InterviewPreparation ->
                false
        }

    private fun handleScreenShowed(state: State): ReducerResult =
        state to buildSet {
            if (shouldLogStepSolvingTime(state)) {
                add(InternalAction.StartSolvingTimer)
            }
            // Fix duplicate analytic events -> StageImplementFeature sends this event
            if (stepRoute !is StepRoute.StageImplement) {
                add(
                    InternalAction.LogAnalyticEvent(
                        StepViewedHyperskillAnalyticEvent(
                            stepRoute.analyticRoute
                        )
                    )
                )
            }
        }

    private fun handleSolvingTimerFired(state: State): ReducerResult =
        if (shouldLogStepSolvingTime(state)) {
            state to setOf(InternalAction.LogSolvingTime(stepRoute.stepId))
        } else {
           state to emptySet()
        }

    private fun handleScreenHidden(state: State): ReducerResult =
        state to getStopSolvingTimerActions(state)

    private fun handleStepCompleted(
        state: State,
        message: InternalMessage.StepCompleted
    ) : ReducerResult =
        if (state is State.Data && message.stepId == stepRoute.stepId) {
            state.copy(
                // Is used to not start timer when the screen is showed
                step = state.step.copy(isCompleted = true)
            ) to getStopSolvingTimerActions(state)
        } else {
            state to emptySet()
        }

    private fun getStopSolvingTimerActions(state: State): Set<Action> =
        buildSet {
            add(InternalAction.StopSolvingTimer)
            if (shouldLogStepSolvingTime(state)) {
                add(InternalAction.LogSolvingTime(stepRoute.stepId))
            }
        }

    private fun shouldLogStepSolvingTime(state: State): Boolean =
        state is State.Data && !state.step.isCompleted && !state.step.isCribbed

    private fun reduceStepCompletionMessage(
        state: StepCompletionFeature.State,
        message: StepCompletionFeature.Message
    ): Pair<StepCompletionFeature.State, Set<Action>> {
        val (stepCompletionState, stepCompletionActions) = stepCompletionReducer.reduce(state, message)

        val actions = stepCompletionActions
            .map {
                if (it is StepCompletionFeature.Action.ViewAction) {
                    Action.ViewAction.StepCompletionViewAction(it)
                } else {
                    InternalAction.StepCompletionAction(it)
                }
            }
            .toSet()

        return stepCompletionState to actions
    }
}
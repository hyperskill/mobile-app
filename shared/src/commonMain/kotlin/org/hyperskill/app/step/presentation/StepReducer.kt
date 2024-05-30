package org.hyperskill.app.step.presentation

import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.learning_activities.presentation.mapper.LearningActivityTargetViewActionMapper
import org.hyperskill.app.step.domain.analytic.StepToolbarActionClickedHyperskillAnalyticEvent
import org.hyperskill.app.step.domain.analytic.StepViewedHyperskillAnalyticEvent
import org.hyperskill.app.step.domain.model.StepMenuAction
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.InternalAction
import org.hyperskill.app.step.presentation.StepFeature.InternalMessage
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step.presentation.StepFeature.State
import org.hyperskill.app.step.presentation.StepFeature.StepState
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_completion.presentation.StepCompletionReducer
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature
import org.hyperskill.app.step_toolbar.presentation.StepToolbarReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class StepReducer(
    private val stepRoute: StepRoute,
    private val stepCompletionReducer: StepCompletionReducer,
    private val stepToolbarReducer: StepToolbarReducer
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            is Message.Initialize ->
                handleInitialize(state, message)
            is Message.StepLoaded.Success ->
                handleStepLoadedSuccess(state, message)
            is Message.StepLoaded.Error ->
                state.copy(stepState = StepState.Error) to emptySet()
            is Message.ScreenShowed ->
                handleScreenShowed(state)
            is Message.ScreenHidden ->
                handleScreenHidden(state)
            is InternalMessage.SolvingTimerFired ->
                handleSolvingTimerFired(state)
            is InternalMessage.StepCompleted ->
                handleStepCompleted(state, message)
            is Message.StepCompletionMessage ->
                if (state.stepState is StepState.Data) {
                    val (stepCompletionState, stepCompletionActions) =
                        reduceStepCompletionMessage(state.stepState.stepCompletionState, message.message)
                    state.updateStepState(
                        state.stepState.copy(stepCompletionState = stepCompletionState)
                    ) to stepCompletionActions
                } else {
                    null
                }
            is Message.StepToolbarMessage -> {
                val (stepToolbarState, stepToolbarActions) =
                    reduceStepToolbarMessage(state.stepToolbarState, message.message)
                state.copy(stepToolbarState = stepToolbarState) to stepToolbarActions
            }

            Message.ShareClicked -> handleShareClicked(state)
            is InternalMessage.ShareLinkReady -> handleShareLinkReady(state, message)

            Message.ReportClicked -> handleReportClicked(state)
            Message.SkipClicked -> handleSkipClicked(state)

            Message.OpenInWebClicked -> handleOpenInWebClicked(state)
            is InternalMessage.GetMagicLinkReceiveSuccess -> handleMagicLinkSuccess(state, message)
            InternalMessage.GetMagicLinkReceiveFailure -> handleMagicLinkError(state)

            InternalMessage.StepSkipSuccess -> handleSkipStepSuccess(state)
            InternalMessage.StepSkipFailed -> handleSkipStepError(state)

            is InternalMessage.FetchNextLearningActivitySuccess ->
                handleFetchNextLearningActivitySuccess(state, message)
            InternalMessage.FetchNextLearningActivityError ->
                handleFetchNextLearningActivityError(state)
        } ?: (state to emptySet())

    private fun handleInitialize(state: State, message: Message.Initialize): ReducerResult {
        val shouldReloadStep =
            state.stepState is StepState.Idle ||
                (message.forceUpdate && (state.stepState is StepState.Data || state.stepState is StepState.Error))
        val (stepState, stepActions) = if (shouldReloadStep) {
            StepState.Loading to setOf(
                InternalAction.FetchStep(stepRoute),
                InternalAction.ViewStep(stepRoute.stepId, stepRoute.stepContext)
            )
        } else {
            state.stepState to emptySet()
        }

        val (stepToolbarState, stepToolbarActions) =
            reduceStepToolbarMessage(
                state.stepToolbarState,
                StepToolbarFeature.InternalMessage.Initialize(stepRoute.topicId, message.forceUpdate)
            )

        return state.copy(
            stepState = stepState,
            stepToolbarState = stepToolbarState
        ) to stepActions + stepToolbarActions
    }

    private fun handleStepLoadedSuccess(state: State, message: Message.StepLoaded.Success): ReducerResult {
        val stepState = StepState.Data(
            step = message.step,
            isPracticingAvailable = isPracticingAvailable(stepRoute),
            stepCompletionState = StepCompletionFeature.createState(message.step, stepRoute)
        )
        val stepActions = buildSet {
            add(InternalAction.UpdateNextLearningActivityState(message.step))
            if (shouldLogStepSolvingTime(stepState)) {
                add(InternalAction.StartSolvingTimer)
            }
        }

        // Initialize step toolbar if no topic_id is provided in the step route
        val (stepToolbarState, stepToolbarActions) =
            reduceStepToolbarMessage(
                state.stepToolbarState,
                StepToolbarFeature.InternalMessage.Initialize(message.step.topic)
            )

        return state.copy(
            stepState = stepState,
            stepToolbarState = stepToolbarState
        ) to stepActions + stepToolbarActions
    }

    private fun isPracticingAvailable(stepRoute: StepRoute): Boolean =
        when (stepRoute) {
            is StepRoute.Learn.Step,
            is StepRoute.Learn.TheoryOpenedFromPractice ->
                true
            is StepRoute.Learn.TheoryOpenedFromSearch,
            is StepRoute.LearnDaily,
            is StepRoute.Repeat.Practice,
            is StepRoute.Repeat.Theory,
            is StepRoute.StageImplement ->
                false
        }

    private fun handleScreenShowed(state: State): ReducerResult =
        state to buildSet {
            if (shouldLogStepSolvingTime(state.stepState)) {
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

    private fun handleScreenHidden(state: State): ReducerResult =
        state to getStopSolvingTimerActions(state.stepState)

    private fun handleSolvingTimerFired(state: State): ReducerResult =
        if (shouldLogStepSolvingTime(state.stepState)) {
            state to setOf(InternalAction.LogSolvingTime(stepRoute.stepId))
        } else {
            state to emptySet()
        }

    private fun getStopSolvingTimerActions(state: StepState): Set<Action> =
        buildSet {
            add(InternalAction.StopSolvingTimer)
            if (shouldLogStepSolvingTime(state)) {
                add(InternalAction.LogSolvingTime(stepRoute.stepId))
            }
        }

    private fun shouldLogStepSolvingTime(state: StepState): Boolean =
        state is StepState.Data && !state.step.isCompleted && !state.step.isCribbed

    private fun handleStepCompleted(
        state: State,
        message: InternalMessage.StepCompleted
    ): ReducerResult =
        if (state.stepState is StepState.Data && message.stepId == stepRoute.stepId) {
            state.updateStepState(
                state.stepState.copy(
                    /**
                     * Is used to not start timer when the screen with a [Message.ScreenShowed]
                     * */
                    step = state.stepState.step.copy(isCompleted = true)
                )
            ) to getStopSolvingTimerActions(state.stepState)
        } else {
            state to emptySet()
        }

    private fun handleShareClicked(state: State): ReducerResult =
        state to setOf(
            InternalAction.CreateStepShareLink(stepRoute),
            InternalAction.LogAnalyticEvent(
                StepToolbarActionClickedHyperskillAnalyticEvent(
                    StepMenuAction.SHARE,
                    stepRoute
                )
            )
        )

    private fun handleShareLinkReady(state: State, message: InternalMessage.ShareLinkReady): ReducerResult =
        state to setOf(Action.ViewAction.ShareStepLink(message.link))

    private fun handleReportClicked(state: State): ReducerResult =
        state to setOf(
            Action.ViewAction.ShowFeedbackModal(stepRoute),
            InternalAction.LogAnalyticEvent(
                StepToolbarActionClickedHyperskillAnalyticEvent(
                    StepMenuAction.REPORT, stepRoute
                )
            )
        )

    private fun handleSkipClicked(state: State): ReducerResult =
        if (!state.isLoadingShowed && state.stepState is StepState.Data) {
            state.copy(isLoadingShowed = true) to setOf(
                InternalAction.LogAnalyticEvent(
                    StepToolbarActionClickedHyperskillAnalyticEvent(StepMenuAction.SKIP, stepRoute)
                ),
                InternalAction.SkipStep(stepRoute.stepId)
            )
        } else {
            state to emptySet()
        }

    private fun handleSkipStepSuccess(state: State): ReducerResult =
        if (state.stepState is StepState.Data) {
            state to setOf(InternalAction.FetchNextLearningActivity)
        } else {
            state to emptySet()
        }

    private fun handleSkipStepError(state: State): ReducerResult =
        state.copy(isLoadingShowed = false) to setOf(Action.ViewAction.ShowLoadingError)

    private fun handleFetchNextLearningActivitySuccess(
        state: State,
        message: InternalMessage.FetchNextLearningActivitySuccess
    ): ReducerResult {
        val nextStepRoute =
            message.nextLearningActivity
                ?.let(LearningActivityTargetViewActionMapper::mapLearningActivityToStepRouteOrNull)

        return state.copy(isLoadingShowed = false) to setOf(
            if (nextStepRoute != null) {
                Action.ViewAction.ReloadStep(nextStepRoute)
            } else {
                Action.ViewAction.ShowCantSkipError
            }
        )
    }

    private fun handleFetchNextLearningActivityError(state: State): ReducerResult =
        state.copy(isLoadingShowed = false) to setOf(Action.ViewAction.ShowLoadingError)

    private fun handleOpenInWebClicked(state: State): ReducerResult =
        state.copy(isLoadingShowed = true) to setOf(
            InternalAction.GetMagicLink(HyperskillUrlPath.Step(stepRoute)),
            InternalAction.LogAnalyticEvent(
                StepToolbarActionClickedHyperskillAnalyticEvent(
                    StepMenuAction.OPEN_IN_WEB, stepRoute
                )
            )
        )

    private fun handleMagicLinkSuccess(
        state: State,
        message: InternalMessage.GetMagicLinkReceiveSuccess
    ): ReducerResult =
        state.copy(isLoadingShowed = false) to setOf(Action.ViewAction.OpenUrl(message.url))

    private fun handleMagicLinkError(
        state: State
    ): ReducerResult =
        state.copy(isLoadingShowed = false) to setOf(Action.ViewAction.ShowLoadingError)

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

    private fun reduceStepToolbarMessage(
        state: StepToolbarFeature.State,
        message: StepToolbarFeature.Message
    ): Pair<StepToolbarFeature.State, Set<Action>> {
        val (stepToolbarState, stepToolbarActions) = stepToolbarReducer.reduce(state, message)

        val actions = stepToolbarActions
            .map {
                if (it is StepToolbarFeature.Action.ViewAction) {
                    Action.ViewAction.StepToolbarViewAction(it)
                } else {
                    InternalAction.StepToolbarAction(it)
                }
            }
            .toSet()

        return stepToolbarState to actions
    }

    private fun State.updateStepState(stepState: StepState): State =
        copy(stepState = stepState)
}
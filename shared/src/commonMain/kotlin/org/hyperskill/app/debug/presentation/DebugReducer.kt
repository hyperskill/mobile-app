package org.hyperskill.app.debug.presentation

import org.hyperskill.app.debug.presentation.DebugFeature.Action
import org.hyperskill.app.debug.presentation.DebugFeature.Message
import org.hyperskill.app.debug.presentation.DebugFeature.State
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class DebugReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle || state is State.Error && message.forceUpdate) {
                    State.Loading to setOf(Action.FetchDebugSettings)
                } else {
                    null
                }
            is Message.FetchDebugSettingsSuccess ->
                if (state is State.Loading) {
                    State.Content(
                        currentEndpointConfig = message.debugSettings.endpointConfigType,
                        selectedEndpointConfig = message.debugSettings.endpointConfigType
                    ) to emptySet()
                } else {
                    null
                }
            is Message.FetchDebugSettingsFailure ->
                if (state is State.Loading) {
                    State.Error to emptySet()
                } else {
                    null
                }
            is Message.SelectEndpointConfig ->
                if (state is State.Content) {
                    state.copy(selectedEndpointConfig = message.endpointConfig) to emptySet()
                } else {
                    null
                }
            // Step navigation
            is Message.StepNavigationInputTextChanged ->
                if (state is State.Content) {
                    state.copy(navigationInput = state.navigationInput.copy(stepId = message.text)) to emptySet()
                } else {
                    null
                }
            is Message.StepNavigationOpenClicked ->
                if (state is State.Content) {
                    val stepId = state.navigationInput.stepId.toLongOrNull()
                    if (stepId != null) {
                        state to setOf(Action.ViewAction.OpenStep(StepRoute.Learn(stepId)))
                    } else {
                        null
                    }
                } else {
                    null
                }
            // Stage implement navigation
            is Message.StageImplementNavigationInputChanged ->
                if (state is State.Content) {
                    state.copy(
                        navigationInput = state.navigationInput.copy(
                            stageImplement = State.Content.NavigationInput.StageImplement(
                                projectId = message.projectId,
                                stageId = message.stageId
                            )
                        )
                    ) to emptySet()
                } else {
                    null
                }
            is Message.StageImplementNavigationOpenClicked ->
                if (state is State.Content) {
                    val projectId = state.navigationInput.stageImplement.projectId.toLongOrNull()
                    val stageId = state.navigationInput.stageImplement.stageId.toLongOrNull()
                    if (projectId != null && stageId != null) {
                        state to setOf(Action.ViewAction.OpenStageImplement(projectId, stageId))
                    } else {
                        null
                    }
                } else {
                    null
                }
            // Apply settings
            is Message.ApplySettingsClicked ->
                if (state is State.Content && state.currentEndpointConfig != state.selectedEndpointConfig) {
                    state to setOf(Action.UpdateEndpointConfig(state.selectedEndpointConfig))
                } else {
                    null
                }
            is Message.ApplySettingsSuccess ->
                if (state is State.Content) {
                    state.copy(
                        currentEndpointConfig = state.selectedEndpointConfig
                    ) to setOf(Action.ViewAction.RestartApplication)
                } else {
                    null
                }
            is Message.ApplySettingsFailure ->
                if (state is State.Content) {
                    state to emptySet()
                } else {
                    null
                }
        } ?: (state to emptySet())
}
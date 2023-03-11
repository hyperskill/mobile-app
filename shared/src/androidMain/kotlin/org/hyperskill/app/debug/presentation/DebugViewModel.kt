package org.hyperskill.app.debug.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.debug.domain.model.EndpointConfigType
import org.hyperskill.app.debug.presentation.DebugFeature.Action.ViewAction
import org.hyperskill.app.debug.presentation.DebugFeature.Message
import org.hyperskill.app.debug.presentation.DebugFeature.ViewState

class DebugViewModel(
    reduxViewContainer: FlowView<ViewState, Message, ViewAction>
) : ReduxFlowViewModel<ViewState, Message, ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(Message.Initialize(forceUpdate = false))
    }

    fun onEndpointConfigClicked(config: EndpointConfigType) {
        onNewMessage(Message.SelectEndpointConfig(config))
    }

    fun onApplyConfigClicked() {
        onNewMessage(Message.ApplySettingsClicked)
    }

    fun onOpenStepClicked() {
        onNewMessage(Message.StepNavigationOpenClicked)
    }

    fun onStepIdChanged(stepId: String) {
        onNewMessage(Message.StepNavigationInputTextChanged(stepId))
    }

    fun onFindStageInputChanged(projectId: String, stageId: String) {
        onNewMessage(Message.StageImplementNavigationInputChanged(projectId = projectId, stageId = stageId))
    }

    fun onOpenStageClick() {
        onNewMessage(Message.StageImplementNavigationOpenClicked)
    }
}
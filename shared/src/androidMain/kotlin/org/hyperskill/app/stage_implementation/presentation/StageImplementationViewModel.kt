package org.hyperskill.app.stage_implementation.presentation

import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StageImplementationViewModel(
    projectId: Long,
    stageId: Long,
    reduxViewContainer: ReduxViewContainer<
        StageImplementFeature.ViewState, StageImplementFeature.Message, StageImplementFeature.Action.ViewAction>
) : ReduxViewModel<
    StageImplementFeature.ViewState,
    StageImplementFeature.Message,
    StageImplementFeature.Action.ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(StageImplementFeature.Message.Initialize(projectId = projectId, stageId = stageId))
    }
}
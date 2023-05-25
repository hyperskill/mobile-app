package org.hyperskill.app.project_selection.presentation

import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Action.ViewAction
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.ViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class ProjectSelectionListViewModel(
    reduxViewContainer: ReduxViewContainer<ViewState, Message, ViewAction>
) : ReduxViewModel<ViewState, Message, ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(Message.Initialize)
    }
}
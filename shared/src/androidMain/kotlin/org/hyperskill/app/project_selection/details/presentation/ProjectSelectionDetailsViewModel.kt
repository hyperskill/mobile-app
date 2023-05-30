package org.hyperskill.app.project_selection.details.presentation

import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Action
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Message
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.ViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class ProjectSelectionDetailsViewModel(
    reduxViewContainer: ReduxViewContainer<ViewState, Message, Action.ViewAction>
) : ReduxViewModel<ViewState, Message, Action.ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(Message.Initialize)
    }
}
package org.hyperskill.app.problems_limit_reached.presentation

import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Action.ViewAction
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Message
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.ViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class ProblemsLimitReachedModalViewModel(
    viewContainer: ReduxViewContainer<ViewState, Message, ViewAction>
) : ReduxViewModel<ViewState, Message, ViewAction>(viewContainer) {
    fun onShownEvent() {
        onNewMessage(Message.ShownEventMessage)
    }

    fun onHiddenEvent() {
        onNewMessage(Message.HiddenEventMessage)
    }

    fun onGoHomeClicked() {
        onNewMessage(Message.GoToHomeScreenClicked)
    }

    fun onUnlockUnlimitedProblemsClicked() {
        onNewMessage(Message.UnlockUnlimitedProblemsClicked)
    }
}
package org.hyperskill.app.problems_limit.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class ProblemsLimitViewModel(
    viewContainer: ReduxViewContainer<ProblemsLimitFeature.ViewState, ProblemsLimitFeature.Message, ProblemsLimitFeature.Action.ViewAction>
) : ReduxViewModel<ProblemsLimitFeature.ViewState, ProblemsLimitFeature.Message, ProblemsLimitFeature.Action.ViewAction>(viewContainer) {
    init {
        onNewMessage(ProblemsLimitFeature.Message.Initialize())
    }
}
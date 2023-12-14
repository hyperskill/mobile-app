package org.hyperskill.app.search.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.search.presentation.SearchFeature.Action.ViewAction
import org.hyperskill.app.search.presentation.SearchFeature.Message
import org.hyperskill.app.search.presentation.SearchFeature.ViewState

class SearchViewModel(
    viewContainer: FlowView<ViewState, Message, ViewAction>
) : ReduxFlowViewModel<ViewState, Message, ViewAction>(viewContainer)
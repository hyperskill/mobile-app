package org.hyperskill.app.search.presentation

import org.hyperskill.app.search.presentation.SearchFeature.Action.ViewAction
import org.hyperskill.app.search.presentation.SearchFeature.Message
import org.hyperskill.app.search.presentation.SearchFeature.ViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class SearchViewModel(
    viewContainer: ReduxViewContainer<ViewState, Message, ViewAction>
) : ReduxViewModel<ViewState, Message, ViewAction>(viewContainer)
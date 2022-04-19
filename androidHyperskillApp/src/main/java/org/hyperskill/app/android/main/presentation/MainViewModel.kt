package org.hyperskill.app.android.main.presentation

import org.hyperskill.app.main.presentation.AppFeature
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class MainViewModel(
    reduxViewContainer: ReduxViewContainer<AppFeature.State, AppFeature.Message, AppFeature.Action.ViewAction>
) : ReduxViewModel<AppFeature.State, AppFeature.Message, AppFeature.Action.ViewAction>(reduxViewContainer)
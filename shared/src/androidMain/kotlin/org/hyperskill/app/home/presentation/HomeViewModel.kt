package org.hyperskill.app.home.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class HomeViewModel(
    reduxViewContainer: ReduxViewContainer<HomeFeature.State, HomeFeature.Message, HomeFeature.Action.ViewAction>
) : ReduxViewModel<HomeFeature.State, HomeFeature.Message, HomeFeature.Action.ViewAction>(reduxViewContainer)
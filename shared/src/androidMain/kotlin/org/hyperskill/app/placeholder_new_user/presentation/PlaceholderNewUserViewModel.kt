package org.hyperskill.app.placeholder_new_user.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class PlaceholderNewUserViewModel(
    reduxViewContainer: ReduxViewContainer<PlaceholderNewUserFeature.State, PlaceholderNewUserFeature.Message, PlaceholderNewUserFeature.Action.ViewAction>
) : ReduxViewModel<PlaceholderNewUserFeature.State, PlaceholderNewUserFeature.Message, PlaceholderNewUserFeature.Action.ViewAction>(reduxViewContainer)

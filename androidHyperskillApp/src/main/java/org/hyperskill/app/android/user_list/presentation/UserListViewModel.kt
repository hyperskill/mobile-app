package org.hyperskill.app.android.user_list.presentation

import org.hyperskill.app.user_list.presentation.UsersListFeature
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class UserListViewModel(
    reduxViewContainer: ReduxViewContainer<UsersListFeature.State, UsersListFeature.Message, UsersListFeature.Action.ViewAction>
) : ReduxViewModel<UsersListFeature.State, UsersListFeature.Message, UsersListFeature.Action.ViewAction>(reduxViewContainer)
package org.hyperskill.app.android.auth.presentation

import org.hyperskill.app.auth.presentation.AuthFeature
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class AuthEmailViewModel(
    reduxViewContainer: ReduxViewContainer<AuthFeature.State, AuthFeature.Message, AuthFeature.Action.ViewAction>
) : ReduxViewModel<AuthFeature.State, AuthFeature.Message, AuthFeature.Action.ViewAction>(reduxViewContainer)
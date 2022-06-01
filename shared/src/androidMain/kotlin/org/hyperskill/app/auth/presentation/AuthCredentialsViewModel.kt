package org.hyperskill.app.auth.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class AuthCredentialsViewModel(
    reduxViewContainer: ReduxViewContainer<AuthCredentialsFeature.State, AuthCredentialsFeature.Message, AuthCredentialsFeature.Action.ViewAction>
) : ReduxViewModel<AuthCredentialsFeature.State, AuthCredentialsFeature.Message, AuthCredentialsFeature.Action.ViewAction>(reduxViewContainer)
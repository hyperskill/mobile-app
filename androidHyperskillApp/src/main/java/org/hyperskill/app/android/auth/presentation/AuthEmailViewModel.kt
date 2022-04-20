package org.hyperskill.app.android.auth.presentation

import org.hyperskill.app.auth.presentation.AuthCredentialsFeature
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class AuthEmailViewModel(
    reduxViewContainer: ReduxViewContainer<AuthCredentialsFeature.State, AuthCredentialsFeature.Message, AuthCredentialsFeature.Action.ViewAction>
) : ReduxViewModel<AuthCredentialsFeature.State, AuthCredentialsFeature.Message, AuthCredentialsFeature.Action.ViewAction>(reduxViewContainer)
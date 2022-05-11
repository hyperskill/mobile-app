package org.hyperskill.app.android.auth.presentation

import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class AuthSocialWebViewViewModel(
    reduxViewContainer: ReduxViewContainer<AuthSocialWebViewFeature.State, AuthSocialWebViewFeature.Message, AuthSocialWebViewFeature.Action>
) : ReduxViewModel<AuthSocialWebViewFeature.State, AuthSocialWebViewFeature.Message, AuthSocialWebViewFeature.Action>(reduxViewContainer)

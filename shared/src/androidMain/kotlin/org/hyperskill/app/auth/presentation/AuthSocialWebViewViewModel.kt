package org.hyperskill.app.auth.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class AuthSocialWebViewViewModel(
    reduxViewContainer: ReduxViewContainer<AuthSocialWebViewFeature.State, AuthSocialWebViewFeature.Message, AuthSocialWebViewFeature.Action>
) : ReduxViewModel<AuthSocialWebViewFeature.State, AuthSocialWebViewFeature.Message, AuthSocialWebViewFeature.Action>(reduxViewContainer)

package org.hyperskill.app.auth.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class AuthSocialViewModel(
    reduxViewContainer: ReduxViewContainer<AuthSocialFeature.State, AuthSocialFeature.Message, AuthSocialFeature.Action.ViewAction>
) : ReduxViewModel<AuthSocialFeature.State, AuthSocialFeature.Message, AuthSocialFeature.Action.ViewAction>(reduxViewContainer)
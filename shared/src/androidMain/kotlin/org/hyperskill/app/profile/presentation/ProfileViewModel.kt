package org.hyperskill.app.profile.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class ProfileViewModel(
    reduxViewContainer: ReduxViewContainer<ProfileFeature.State, ProfileFeature.Message, ProfileFeature.Action.ViewAction>
) : ReduxViewModel<ProfileFeature.State, ProfileFeature.Message, ProfileFeature.Action.ViewAction>(reduxViewContainer)
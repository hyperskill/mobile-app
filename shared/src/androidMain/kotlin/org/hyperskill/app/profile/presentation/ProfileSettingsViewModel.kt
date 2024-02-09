package org.hyperskill.app.profile.presentation

import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action.ViewAction
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.ViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class ProfileSettingsViewModel(
    reduxViewContainer: ReduxViewContainer<ViewState, Message, ViewAction>
) : ReduxViewModel<ViewState, Message, ViewAction>(reduxViewContainer)
package org.hyperskill.app.profile.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class ProfileSettingsViewModel(
    reduxViewContainer: ReduxViewContainer<ProfileSettingsFeature.State, ProfileSettingsFeature.Message, ProfileSettingsFeature.Action.ViewAction>
) : ReduxViewModel<ProfileSettingsFeature.State, ProfileSettingsFeature.Message, ProfileSettingsFeature.Action.ViewAction>(reduxViewContainer)

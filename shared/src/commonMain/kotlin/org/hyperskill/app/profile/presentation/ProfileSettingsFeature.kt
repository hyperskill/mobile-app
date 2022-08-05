package org.hyperskill.app.profile.presentation

import org.hyperskill.app.profile.domain.model.ProfileSettings
import org.hyperskill.app.profile.domain.model.Theme

interface ProfileSettingsFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(val profileSettings: ProfileSettings) : State
        object Error : State
    }

    sealed interface Message {
        data class InitMessage(val forceUpdate: Boolean = false) : Message
        data class ProfileSettingsSuccess(val profileSettings: ProfileSettings) : Message
        object ProfileSettingsError : Message
        data class ThemeChanged(val theme: Theme) : Message
        object LoggedOut : Message
    }

    sealed interface Action {
        object FetchProfileSettings : Action
        data class ChangeTheme(val theme: Theme) : Action
        object Logout : Action
        sealed interface ViewAction : Action {
            object Logout : ViewAction
        }
    }
}
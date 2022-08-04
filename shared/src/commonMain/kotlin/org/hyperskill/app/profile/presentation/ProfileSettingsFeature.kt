package org.hyperskill.app.profile.presentation

import org.hyperskill.app.profile.domain.model.ProfileSettings

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
    }

    sealed interface Action {
        object FetchProfileSettings : Action
        sealed class ViewAction : Action
    }
}
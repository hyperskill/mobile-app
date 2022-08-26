package org.hyperskill.app.profile_settings.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.profile_settings.domain.model.ProfileSettings
import org.hyperskill.app.profile_settings.domain.model.Theme

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
        object LogoutConfirmed : Message

        /**
         * Analytic
         */
        object ProfileSettingsViewedEventMessage : Message
        object ProfileSettingsClickedDoneEventMessage : Message
        object ProfileSettingsClickedThemeEventMessage : Message
        object ProfileSettingsClickedTermsOfServiceEventMessage : Message
        object ProfileSettingsClickedPrivacyPolicyEventMessage : Message
        object ProfileSettingsClickedHelpCenterEventMessage : Message
        object ProfileSettingsClickedLogoutEventMessage : Message
        object ProfileSettingsClickedDeleteAccountEventMessage : Message
    }

    sealed interface Action {
        object FetchProfileSettings : Action
        data class ChangeTheme(val theme: Theme) : Action
        object Logout : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed class ViewAction : Action
    }
}
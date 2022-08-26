package org.hyperskill.app.profile_settings.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsClickedHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsDeleteAccountNoticeHiddenHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsDeleteAccountNoticeShownHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsViewedHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class ProfileSettingsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.InitMessage -> {
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.Error))
                ) {
                    State.Loading to setOf(Action.FetchProfileSettings)
                } else {
                    null
                }
            }
            is Message.ProfileSettingsSuccess ->
                State.Content(message.profileSettings) to emptySet()
            is Message.ProfileSettingsError ->
                State.Error to emptySet()
            is Message.ThemeChanged ->
                if (state is State.Content) {
                    State.Content(state.profileSettings.copy(theme = message.theme)) to
                        setOf(Action.ChangeTheme(message.theme))
                } else {
                    null
                }
            is Message.LogoutConfirmed ->
                state to setOf(Action.Logout)
            is Message.ProfileSettingsViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(ProfileSettingsViewedHyperskillAnalyticEvent()))
            is Message.ProfileSettingsClickedDoneEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            HyperskillAnalyticPart.HEAD,
                            HyperskillAnalyticTarget.DONE
                        )
                    )
                )
            is Message.ProfileSettingsClickedThemeEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.THEME
                        )
                    )
                )
            is Message.ProfileSettingsClickedTermsOfServiceEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.JETBRAINS_TERMS_OF_SERVICE
                        )
                    )
                )
            is Message.ProfileSettingsClickedPrivacyPolicyEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.HYPERSKILL_TERMS_OF_SERVICE
                        )
                    )
                )
            is Message.ProfileSettingsClickedHelpCenterEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.HELP_CENTER
                        )
                    )
                )
            is Message.ProfileSettingsClickedLogoutEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.LOGOUT
                        )
                    )
                )
            is Message.ProfileSettingsClickedDeleteAccountEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsClickedHyperskillAnalyticEvent(
                            target = HyperskillAnalyticTarget.DELETE_ACCOUNT
                        )
                    )
                )
            is Message.ProfileSettingsDeleteAccountNoticeShownEventMessage ->
                state to setOf(Action.LogAnalyticEvent(ProfileSettingsDeleteAccountNoticeShownHyperskillAnalyticEvent()))
            is Message.ProfileSettingsDeleteAccountNoticeHiddenEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProfileSettingsDeleteAccountNoticeHiddenHyperskillAnalyticEvent(
                            message.isConfirmed
                        )
                    )
                )
        } ?: (state to emptySet())
}
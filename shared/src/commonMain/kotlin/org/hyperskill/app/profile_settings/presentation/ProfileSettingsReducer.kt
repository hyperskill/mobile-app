package org.hyperskill.app.profile_settings.presentation

import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsClickedDoneHyperskillAnalyticEvent
import org.hyperskill.app.profile_settings.domain.analytic.ProfileSettingsClickedThemeHyperskillAnalyticEvent
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
                state to
                    setOf(Action.LogAnalyticEvent(ProfileSettingsClickedDoneHyperskillAnalyticEvent()))
            is Message.ProfileSettingsClickedThemeEventMessage ->
                state to
                    setOf(Action.LogAnalyticEvent(ProfileSettingsClickedThemeHyperskillAnalyticEvent()))
        } ?: (state to emptySet())
}
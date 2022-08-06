package org.hyperskill.app.profile_settings.presentation

import ru.nobird.app.presentation.redux.reducer.StateReducer
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.State
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message

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
                    State.Content(state.profileSettings.copy(theme = message.theme)) to setOf(Action.ChangeTheme(message.theme))
                } else {
                    null
                }
        } ?: (state to emptySet())
}
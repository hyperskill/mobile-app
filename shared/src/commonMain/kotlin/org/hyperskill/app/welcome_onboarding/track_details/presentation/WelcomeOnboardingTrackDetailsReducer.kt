package org.hyperskill.app.welcome_onboarding.track_details.presentation

import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Action
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.InternalAction
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.InternalMessage.SelectTrackFailed
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.InternalMessage.SelectTrackSuccess
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Message
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias WelcomeOnboardingTrackDetailsReducerResult = Pair<State, Set<Action>>

internal class WelcomeOnboardingTrackDetailsReducer : StateReducer<State, Message, Action> {

    companion object {
        private const val JAVA_TRACK_ID = 8L
        private const val JS_TRACK_ID = 32L
        private const val KOTLIN_TRACK_ID = 69L
        private const val PYTHON_TRACK_ID = 6L
        private const val SQL_TRACK_ID = 31L
    }

    override fun reduce(state: State, message: Message): WelcomeOnboardingTrackDetailsReducerResult =
        when (message) {
            Message.ContinueClicked -> handleContinueClicked(state)
            SelectTrackSuccess -> handleSelectTrackSuccess(state)
            SelectTrackFailed -> handleSelectTrackFailed(state)
        }

    private fun handleContinueClicked(state: State): WelcomeOnboardingTrackDetailsReducerResult =
        state.copy(isLoadingShowed = true) to setOf(InternalAction.SelectTrack(getTrackId(state.track)))

    private fun handleSelectTrackSuccess(state: State): WelcomeOnboardingTrackDetailsReducerResult =
        state.copy(isLoadingShowed = false) to setOf(Action.ViewAction.NotifyTrackSelected)

    private fun handleSelectTrackFailed(state: State): WelcomeOnboardingTrackDetailsReducerResult =
        state.copy(isLoadingShowed = false) to setOf(Action.ViewAction.ShowTrackSelectionError)

    private fun getTrackId(track: WelcomeOnboardingTrack): Long =
        when (track) {
            WelcomeOnboardingTrack.JAVA -> JAVA_TRACK_ID
            WelcomeOnboardingTrack.JAVA_SCRIPT -> JS_TRACK_ID
            WelcomeOnboardingTrack.KOTLIN -> KOTLIN_TRACK_ID
            WelcomeOnboardingTrack.PYTHON -> PYTHON_TRACK_ID
            WelcomeOnboardingTrack.SQL -> SQL_TRACK_ID
        }
}
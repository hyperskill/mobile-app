package org.hyperskill.app.welcome_onboarding.track_details.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack

object WelcomeOnboardingTrackDetailsFeature {
    internal data class State(
        val track: WelcomeOnboardingTrack,
        val isLoadingShowed: Boolean
    )

    internal fun initialState(track: WelcomeOnboardingTrack) =
        State(
            track = track,
            isLoadingShowed = false
        )

    /**
     * @param trackDescriptionHtml represents an html string that contains a `<b>` tag.
     */
    data class ViewState(
        val track: WelcomeOnboardingTrack,
        val title: String,
        val trackTitle: String,
        val trackDescriptionHtml: String,
        val changeText: String,
        val buttonText: String,
        val isLoadingShowed: Boolean
    )

    sealed interface Message {
        object ViewedEventMessage : Message
        object ContinueClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        object SelectTrackSuccess : InternalMessage
        object SelectTrackFailed : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object NotifyTrackSelected : ViewAction
            object ShowTrackSelectionError : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction
        data class SelectTrack(val trackId: Long) : InternalAction
    }
}
package org.hyperskill.app.welcome_onboarding.track_details.injection

import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Action
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Message
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface WelcomeOnboardingTrackDetailsComponent {
    val welcomeOnboardingTrackDetailsFeature: Feature<ViewState, Message, Action>
}
package org.hyperskill.app.welcome_onboarding.root.view

import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack

/**
 * @param trackDescriptionHtml represents an html string that contains a `<b>` tag.
 */
data class WelcomeOnboardingTrackViewState(
    val track: WelcomeOnboardingTrack,
    val title: String,
    val trackTitle: String,
    val trackDescriptionHtml: String,
    val changeText: String,
    val buttonText: String
)

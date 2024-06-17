package org.hyperskill.app.welcome_onboarding.finish.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.track_details.view.titleStringResource

class WelcomeOnboardingFinishViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(track: WelcomeOnboardingTrack): WelcomeOnboardingFinishViewState =
        WelcomeOnboardingFinishViewState(
            title = resourceProvider.getString(SharedResources.strings.welcome_onboarding_finish_screen_title),
            description = resourceProvider.getString(
                SharedResources.strings.welcome_onboarding_finish_screen_description,
                resourceProvider.getString(track.titleStringResource)
            )
        )
}
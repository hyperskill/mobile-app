package org.hyperskill.app.welcome_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.welcome_onboarding.view.WelcomeOnboardingTrackViewStateMapper

class WelcomeOnboardingTrackDetailsComponentImpl(
    private val appGraph: AppGraph
): WelcomeOnboardingTrackDetailsComponent {
    override val welcomeOnboardingTrackDetailsViewStateMapper: WelcomeOnboardingTrackViewStateMapper
        get() = WelcomeOnboardingTrackViewStateMapper(appGraph.commonComponent.resourceProvider)
}
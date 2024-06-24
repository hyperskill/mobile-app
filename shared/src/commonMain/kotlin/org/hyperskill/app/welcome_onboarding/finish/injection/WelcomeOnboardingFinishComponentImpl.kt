package org.hyperskill.app.welcome_onboarding.finish.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.welcome_onboarding.finish.view.WelcomeOnboardingFinishViewStateMapper

internal class WelcomeOnboardingFinishComponentImpl(
    private val appGraph: AppGraph
) : WelcomeOnboardingFinishComponent {
    override val welcomeOnboardingFinishViewStateMapper: WelcomeOnboardingFinishViewStateMapper
        get() = WelcomeOnboardingFinishViewStateMapper(appGraph.commonComponent.resourceProvider)
}
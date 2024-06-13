package org.hyperskill.app.welcome_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.welcome_onboarding.view.WelcomeQuestionnaireViewStateMapper

class WelcomeQuestionnaireComponentImpl(
    private val appGraph: AppGraph
) : WelcomeQuestionnaireComponent {
    override val welcomeQuestionnaireViewStateMapper: WelcomeQuestionnaireViewStateMapper
        get() = WelcomeQuestionnaireViewStateMapper(
            resourceProvider = appGraph.commonComponent.resourceProvider,
            platformType = appGraph.commonComponent.platform.platformType
        )
}
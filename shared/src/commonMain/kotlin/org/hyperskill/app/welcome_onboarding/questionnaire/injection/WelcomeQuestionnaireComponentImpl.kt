package org.hyperskill.app.welcome_onboarding.questionnaire.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.welcome_onboarding.questionnaire.view.WelcomeQuestionnaireViewStateMapper

internal class WelcomeQuestionnaireComponentImpl(
    private val appGraph: AppGraph
) : WelcomeQuestionnaireComponent {
    override val welcomeQuestionnaireViewStateMapper: WelcomeQuestionnaireViewStateMapper
        get() = WelcomeQuestionnaireViewStateMapper(
            platform = appGraph.commonComponent.platform,
            resourceProvider = appGraph.commonComponent.resourceProvider
        )
}
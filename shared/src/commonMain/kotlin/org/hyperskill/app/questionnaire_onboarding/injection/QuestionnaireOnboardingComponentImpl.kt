package org.hyperskill.app.questionnaire_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.Action
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.Message
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class QuestionnaireOnboardingComponentImpl(
    private val appGraph: AppGraph
) : QuestionnaireOnboardingComponent {
    override val questionnaireOnboardingFeature: Feature<ViewState, Message, Action>
        get() = QuestionnaireOnboardingFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            logger = appGraph.loggerComponent.logger,
            platform = appGraph.commonComponent.platform,
            resourceProvider = appGraph.commonComponent.resourceProvider
        )
}
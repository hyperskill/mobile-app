package org.hyperskill.app.users_questionnaire_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.users_questionnaire_onboarding.presentation.UsersQuestionnaireOnboardingFeature.Action
import org.hyperskill.app.users_questionnaire_onboarding.presentation.UsersQuestionnaireOnboardingFeature.Message
import org.hyperskill.app.users_questionnaire_onboarding.presentation.UsersQuestionnaireOnboardingFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class UsersQuestionnaireOnboardingComponentImpl(
    private val appGraph: AppGraph
) : UsersQuestionnaireOnboardingComponent {
    override val usersQuestionnaireOnboardingFeature: Feature<ViewState, Message, Action>
        get() = UsersQuestionnaireOnboardingFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            logger = appGraph.loggerComponent.logger,
            platform = appGraph.commonComponent.platform,
            resourceProvider = appGraph.commonComponent.resourceProvider
        )
}
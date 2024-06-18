package org.hyperskill.app.users_questionnaire_onboarding_legacy.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Action
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Message
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

@Deprecated("Should be removed in ALTAPPS-1276")
internal class LegacyUsersQuestionnaireOnboardingComponentImpl(
    private val appGraph: AppGraph
) : LegacyUsersQuestionnaireOnboardingComponent {
    override val legacyUsersQuestionnaireOnboardingFeature: Feature<ViewState, Message, Action>
        get() = LegacyUsersQuestionnaireOnboardingFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            logger = appGraph.loggerComponent.logger,
            platform = appGraph.commonComponent.platform,
            resourceProvider = appGraph.commonComponent.resourceProvider
        )
}
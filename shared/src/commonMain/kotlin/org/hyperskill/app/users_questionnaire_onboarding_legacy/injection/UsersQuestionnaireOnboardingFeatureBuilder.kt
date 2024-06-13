package org.hyperskill.app.users_questionnaire_onboarding_legacy.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingActionDispatcher
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Action
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Message
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.ViewState
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingReducer
import org.hyperskill.app.users_questionnaire_onboarding_legacy.view.mapper.UsersQuestionnaireOnboardingViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

@Deprecated("Should be removed in ALTAPPS-1276")
internal object UsersQuestionnaireOnboardingFeatureBuilder {
    private const val LOG_TAG = "UsersQuestionnaireOnboardingFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        buildVariant: BuildVariant,
        logger: Logger,
        platform: Platform,
        resourceProvider: ResourceProvider
    ): Feature<ViewState, Message, Action> {
        val reducer = LegacyUsersQuestionnaireOnboardingReducer(resourceProvider)
            .wrapWithLogger(buildVariant, logger, LOG_TAG)
        val actionDispatcher = LegacyUsersQuestionnaireOnboardingActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = analyticInteractor
        )

        val viewStateMapper = UsersQuestionnaireOnboardingViewStateMapper(
            platform = platform,
            resourceProvider = resourceProvider
        )

        return ReduxFeature(
            initialState = LegacyUsersQuestionnaireOnboardingFeature.State(),
            reducer = reducer
        )
            .wrapWithActionDispatcher(actionDispatcher)
            .transformState(viewStateMapper::mapState)
    }
}
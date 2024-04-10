package org.hyperskill.app.users_questionnaire_onboarding.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.users_questionnaire_onboarding.presentation.UsersQuestionnaireOnboardingActionDispatcher
import org.hyperskill.app.users_questionnaire_onboarding.presentation.UsersQuestionnaireOnboardingFeature
import org.hyperskill.app.users_questionnaire_onboarding.presentation.UsersQuestionnaireOnboardingFeature.Action
import org.hyperskill.app.users_questionnaire_onboarding.presentation.UsersQuestionnaireOnboardingFeature.Message
import org.hyperskill.app.users_questionnaire_onboarding.presentation.UsersQuestionnaireOnboardingFeature.ViewState
import org.hyperskill.app.users_questionnaire_onboarding.presentation.UsersQuestionnaireOnboardingReducer
import org.hyperskill.app.users_questionnaire_onboarding.view.mapper.UsersQuestionnaireOnboardingViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object UsersQuestionnaireOnboardingFeatureBuilder {
    private const val LOG_TAG = "UsersQuestionnaireOnboardingFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        buildVariant: BuildVariant,
        logger: Logger,
        platform: Platform,
        resourceProvider: ResourceProvider
    ): Feature<ViewState, Message, Action> {
        val reducer = UsersQuestionnaireOnboardingReducer(resourceProvider)
            .wrapWithLogger(buildVariant, logger, LOG_TAG)
        val actionDispatcher = UsersQuestionnaireOnboardingActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = analyticInteractor
        )

        val viewStateMapper = UsersQuestionnaireOnboardingViewStateMapper(
            platform = platform,
            resourceProvider = resourceProvider
        )

        return ReduxFeature(
            initialState = UsersQuestionnaireOnboardingFeature.State(),
            reducer = reducer
        )
            .wrapWithActionDispatcher(actionDispatcher)
            .transformState(viewStateMapper::mapState)
    }
}
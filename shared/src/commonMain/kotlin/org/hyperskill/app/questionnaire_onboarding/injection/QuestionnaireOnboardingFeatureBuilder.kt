package org.hyperskill.app.questionnaire_onboarding.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingActionDispatcher
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.Action
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.Message
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingFeature.ViewState
import org.hyperskill.app.questionnaire_onboarding.presentation.QuestionnaireOnboardingReducer
import org.hyperskill.app.questionnaire_onboarding.view.mapper.QuestionnaireOnboardingViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object QuestionnaireOnboardingFeatureBuilder {
    private const val LOG_TAG = "QuestionnaireOnboardingFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        buildVariant: BuildVariant,
        logger: Logger,
        platform: Platform,
        resourceProvider: ResourceProvider
    ): Feature<ViewState, Message, Action> {
        val reducer = QuestionnaireOnboardingReducer()
            .wrapWithLogger(buildVariant, logger, LOG_TAG)
        val actionDispatcher = QuestionnaireOnboardingActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = analyticInteractor
        )

        val viewStateMapper = QuestionnaireOnboardingViewStateMapper(
            platform = platform,
            resourceProvider = resourceProvider
        )

        return ReduxFeature(
            initialState = QuestionnaireOnboardingFeature.State(),
            reducer = reducer
        )
            .wrapWithActionDispatcher(actionDispatcher)
            .transformState(viewStateMapper::mapState)
    }
}
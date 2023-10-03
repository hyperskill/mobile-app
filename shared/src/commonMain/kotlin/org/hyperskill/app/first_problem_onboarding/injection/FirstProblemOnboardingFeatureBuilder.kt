package org.hyperskill.app.first_problem_onboarding.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingActionDispatcher
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Action
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Message
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.ViewState
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingReducer
import org.hyperskill.app.first_problem_onboarding.view.mapper.FirstProblemOnboardingViewStateMapper
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object FirstProblemOnboardingFeatureBuilder {
    private const val LOG_TAG = "FirstProblemOnboardingFeature"

    fun build(
        isNewUserMode: Boolean,
        analyticInteractor: AnalyticInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        learningActivitiesRepository: LearningActivitiesRepository,
        resourceProvider: ResourceProvider,
        buildVariant: BuildVariant,
        logger: Logger
    ): Feature<ViewState, Message, Action> {
        val reducer = FirstProblemOnboardingReducer().wrapWithLogger(buildVariant, logger, LOG_TAG)

        val actionDispatcher = FirstProblemOnboardingActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = analyticInteractor,
            currentProfileStateRepository = currentProfileStateRepository,
            learningActivityRepository = learningActivitiesRepository
        )

        val firstProblemOnboardingViewStateMapper = FirstProblemOnboardingViewStateMapper(resourceProvider)

        return ReduxFeature(FirstProblemOnboardingFeature.initialState(isNewUserMode), reducer)
            .wrapWithActionDispatcher(actionDispatcher)
            .transformState(firstProblemOnboardingViewStateMapper::map)
    }
}
package org.hyperskill.app.problems_limit.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitActionDispatcher
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.step.domain.model.StepRoute

internal class ProblemsLimitComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : ProblemsLimitComponent {

    companion object {
        private const val LOG_TAG = "ProblemsLimitFeature"
    }

    override val problemsLimitReducer: ProblemsLimitReducer
        get() = ProblemsLimitReducer(stepRoute)

    override val problemsLimitActionDispatcher: ProblemsLimitActionDispatcher
        get() = ProblemsLimitActionDispatcher(
            config = ActionDispatcherOptions(),
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger.withTag(LOG_TAG)
        )
}
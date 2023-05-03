package org.hyperskill.app.problems_limit.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitActionDispatcher
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer

class ProblemsLimitComponentImpl(
    private val screen: ProblemsLimitScreen,
    private val appGraph: AppGraph
) : ProblemsLimitComponent {
    override val problemsLimitReducer: ProblemsLimitReducer
        get() = ProblemsLimitReducer(screen)

    override val problemsLimitActionDispatcher: ProblemsLimitActionDispatcher
        get() = ProblemsLimitActionDispatcher(
            ActionDispatcherOptions(),
            appGraph.buildFreemiumDataComponent().freemiumInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository
        )
}
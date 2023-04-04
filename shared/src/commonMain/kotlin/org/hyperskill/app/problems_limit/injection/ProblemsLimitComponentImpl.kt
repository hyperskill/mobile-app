package org.hyperskill.app.problems_limit.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import ru.nobird.app.presentation.redux.feature.Feature

class ProblemsLimitComponentImpl(private val appGraph: AppGraph) : ProblemsLimitComponent {
    override val problemsLimitFeature: Feature<ProblemsLimitFeature.ViewState, ProblemsLimitFeature.Message, ProblemsLimitFeature.Action>
        get() = ProblemsLimitFeatureBuilder.build(
            appGraph.buildFreemiumDataComponent().freemiumInteractor,
            appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            appGraph.commonComponent.resourceProvider,
            appGraph.commonComponent.dateFormatter
        )
}
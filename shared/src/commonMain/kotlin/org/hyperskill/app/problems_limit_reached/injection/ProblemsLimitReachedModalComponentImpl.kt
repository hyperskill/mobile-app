package org.hyperskill.app.problems_limit_reached.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.problems_limit_reached.domain.model.ProblemsLimitReachedModalFeatureParams
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Action
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Message
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class ProblemsLimitReachedModalComponentImpl(
    private val appGraph: AppGraph,
    private val params: ProblemsLimitReachedModalFeatureParams
) : ProblemsLimitReachedModalComponent {
    override val problemsLimitReachedModalFeature: Feature<ViewState, Message, Action>
        get() = ProblemsLimitReachedModalFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            params = params
        )
}
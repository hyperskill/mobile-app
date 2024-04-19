package org.hyperskill.app.problems_limit_info.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalFeatureParams
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.Action
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.Message
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class ProblemsLimitInfoModalComponentImpl(
    private val appGraph: AppGraph,
    private val params: ProblemsLimitInfoModalFeatureParams
) : ProblemsLimitInfoModalComponent {
    override val problemsLimitInfoModalFeature: Feature<ViewState, Message, Action>
        get() = ProblemsLimitInfoModalFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            dateFormatter = appGraph.commonComponent.dateFormatter,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            params = params
        )
}
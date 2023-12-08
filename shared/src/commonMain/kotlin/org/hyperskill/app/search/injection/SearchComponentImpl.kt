package org.hyperskill.app.search.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.search.presentation.SearchFeature
import ru.nobird.app.presentation.redux.feature.Feature

internal class SearchComponentImpl(private val appGraph: AppGraph) : SearchComponent {
    override val searchFeature: Feature<SearchFeature.ViewState, SearchFeature.Message, SearchFeature.Action>
        get() = SearchFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
        )
}
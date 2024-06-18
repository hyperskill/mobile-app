package org.hyperskill.app.search.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.search.domain.interactor.SearchInteractor
import org.hyperskill.app.search.presentation.SearchActionDispatcher
import org.hyperskill.app.search.presentation.SearchFeature
import org.hyperskill.app.search.presentation.SearchReducer
import org.hyperskill.app.search.view.mapper.SearchViewStateMapper
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object SearchFeatureBuilder {
    private const val LOG_TAG = "SearchFeature"

    fun build(
        searchInteractor: SearchInteractor,
        sentryInteractor: SentryInteractor,
        analyticInteractor: AnalyticInteractor,
        resourceProvider: ResourceProvider,
        logger: Logger,
        buildVariant: BuildVariant,
    ): Feature<SearchFeature.ViewState, SearchFeature.Message, SearchFeature.Action> {
        val searchReducer = SearchReducer(
            resourceProvider = resourceProvider
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)

        val searchActionDispatcher = SearchActionDispatcher(
            config = ActionDispatcherOptions(),
            searchInteractor = searchInteractor,
            sentryInteractor = sentryInteractor
        )

        return ReduxFeature(
            initialState = SearchFeature.initialState(),
            reducer = searchReducer
        )
            .transformState(SearchViewStateMapper::map)
            .wrapWithActionDispatcher(searchActionDispatcher)
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? SearchFeature.InternalAction.LogAnalyticEvent)?.analyticEvent
            }
    }
}
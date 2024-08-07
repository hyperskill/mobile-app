package org.hyperskill.app.track_selection.list.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track_selection.details.domain.repository.TrackSelectionDetailsRepository
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListActionDispatcher
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.InternalAction
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Message
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListReducer
import org.hyperskill.app.track_selection.list.view.TrackSelectionListViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object TrackSelectionListFeatureBuilder {
    private const val LOG_TAG = "TrackSelectionListFeature"

    fun build(
        params: TrackSelectionListParams,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        trackInteractor: TrackInteractor,
        progressesInteractor: ProgressesInteractor,
        trackSelectionDetailsRepository: TrackSelectionDetailsRepository,
        currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
        trackListViewStateMapper: TrackSelectionListViewStateMapper,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<TrackSelectionListFeature.ViewState, Message, Action> {
        val trackSelectionListReducer = TrackSelectionListReducer(params).wrapWithLogger(buildVariant, logger, LOG_TAG)
        val trackSelectionListActionDispatcher = TrackSelectionListActionDispatcher(
            config = ActionDispatcherOptions(),
            sentryInteractor = sentryInteractor,
            trackInteractor = trackInteractor,
            progressesInteractor = progressesInteractor,
            trackSelectionDetailsRepository = trackSelectionDetailsRepository,
            currentStudyPlanStateRepository = currentStudyPlanStateRepository
        )

        return ReduxFeature(TrackSelectionListFeature.State.Idle, trackSelectionListReducer)
            .wrapWithActionDispatcher(trackSelectionListActionDispatcher)
            .transformState(trackListViewStateMapper::map)
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? InternalAction.LogAnalyticEvent)?.analyticEvent
            }
    }
}
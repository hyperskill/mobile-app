package org.hyperskill.app.track_list.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track_list.presentation.TrackListActionDispatcher
import org.hyperskill.app.track_list.presentation.TrackListFeature
import org.hyperskill.app.track_list.presentation.TrackListFeature.Action
import org.hyperskill.app.track_list.presentation.TrackListFeature.Message
import org.hyperskill.app.track_list.presentation.TrackListReducer
import org.hyperskill.app.track_list.view.TrackListViewState
import org.hyperskill.app.track_list.view.TrackListViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object TrackListFeatureBuilder {
    fun build(
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        trackInteractor: TrackInteractor,
        progressesInteractor: ProgressesInteractor,
        profileInteractor: ProfileInteractor,
        freemiumInteractor: FreemiumInteractor,
        currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
        trackListViewStateMapper: TrackListViewStateMapper
    ): Feature<TrackListViewState, Message, Action> {
        val trackListReducer = TrackListReducer()
        val trackListActionDispatcher = TrackListActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor,
            sentryInteractor,
            trackInteractor,
            progressesInteractor,
            profileInteractor,
            freemiumInteractor,
            currentStudyPlanStateRepository
        )

        return ReduxFeature(TrackListFeature.State.Idle, trackListReducer)
            .wrapWithActionDispatcher(trackListActionDispatcher)
            .transformState(trackListViewStateMapper::map)
    }
}
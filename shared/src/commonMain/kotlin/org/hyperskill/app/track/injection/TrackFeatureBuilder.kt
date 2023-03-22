package org.hyperskill.app.track.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.injection.StateRepositoriesComponent
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextActionDispatcher
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextReducer
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import org.hyperskill.app.track.presentation.TrackActionDispatcher
import org.hyperskill.app.track.presentation.TrackFeature
import org.hyperskill.app.track.presentation.TrackReducer
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object TrackFeatureBuilder {
    fun build(
        trackInteractor: TrackInteractor,
        studyPlanInteractor: StudyPlanInteractor,
        profileInteractor: ProfileInteractor,
        progressesInteractor: ProgressesInteractor,
        stateRepositoriesComponent: StateRepositoriesComponent,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        urlPathProcessor: UrlPathProcessor,
        gamificationToolbarReducer: GamificationToolbarReducer,
        gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher,
        topicsToDiscoverNextReducer: TopicsToDiscoverNextReducer,
        topicsToDiscoverNextActionDispatcher: TopicsToDiscoverNextActionDispatcher
    ): Feature<TrackFeature.State, TrackFeature.Message, TrackFeature.Action> {
        val trackReducer = TrackReducer(gamificationToolbarReducer, topicsToDiscoverNextReducer)
        val trackActionDispatcher = TrackActionDispatcher(
            ActionDispatcherOptions(),
            trackInteractor,
            studyPlanInteractor,
            profileInteractor,
            progressesInteractor,
            stateRepositoriesComponent,
            analyticInteractor,
            sentryInteractor,
            urlPathProcessor
        )

        return ReduxFeature(
            TrackFeature.State(
                trackState = TrackFeature.TrackState.Idle,
                toolbarState = GamificationToolbarFeature.State.Idle,
                topicsToDiscoverNextState = TopicsToDiscoverNextFeature.State.Idle
            ),
            trackReducer
        )
            .wrapWithActionDispatcher(trackActionDispatcher)
            .wrapWithActionDispatcher(
                gamificationToolbarActionDispatcher.transform(
                    transformAction = { it.safeCast<TrackFeature.Action.GamificationToolbarAction>()?.action },
                    transformMessage = TrackFeature.Message::GamificationToolbarMessage
                )
            )
            .wrapWithActionDispatcher(
                topicsToDiscoverNextActionDispatcher.transform(
                    transformAction = { it.safeCast<TrackFeature.Action.TopicsToDiscoverNextAction>()?.action },
                    transformMessage = TrackFeature.Message::TopicsToDiscoverNextMessage
                )
            )
    }
}
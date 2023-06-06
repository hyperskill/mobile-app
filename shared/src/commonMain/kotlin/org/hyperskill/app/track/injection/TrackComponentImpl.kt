package org.hyperskill.app.track.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.topics_to_discover_next.domain.model.TopicsToDiscoverNextScreen
import org.hyperskill.app.topics_to_discover_next.injection.TopicsToDiscoverNextComponent
import org.hyperskill.app.track.presentation.TrackFeature
import ru.nobird.app.presentation.redux.feature.Feature

class TrackComponentImpl(private val appGraph: AppGraph) : TrackComponent {
    private val gamificationToolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent(GamificationToolbarScreen.TRACK)

    private val topicsToDiscoverNextComponent: TopicsToDiscoverNextComponent =
        appGraph.buildTopicsToDiscoverNextComponent(TopicsToDiscoverNextScreen.TRACK)

    override val trackFeature: Feature<TrackFeature.State, TrackFeature.Message, TrackFeature.Action>
        get() = TrackFeatureBuilder.build(
            appGraph.buildTrackDataComponent().trackInteractor,
            appGraph.buildStudyPlanDataComponent().studyPlanInteractor,
            appGraph.buildProfileDataComponent().currentProfileStateRepository,
            appGraph.buildProgressesDataComponent().progressesInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.buildMagicLinksDataComponent().urlPathProcessor,
            gamificationToolbarComponent.gamificationToolbarReducer,
            gamificationToolbarComponent.gamificationToolbarActionDispatcher,
            topicsToDiscoverNextComponent.topicsToDiscoverNextReducer,
            topicsToDiscoverNextComponent.topicsToDiscoverNextActionDispatcher
        )
}
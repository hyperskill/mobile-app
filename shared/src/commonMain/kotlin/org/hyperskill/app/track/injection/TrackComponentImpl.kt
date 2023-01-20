package org.hyperskill.app.track.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.track.presentation.TrackFeature
import ru.nobird.app.presentation.redux.feature.Feature

class TrackComponentImpl(private val appGraph: AppGraph) : TrackComponent {
    private val gamificationToolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent()

    override val trackFeature: Feature<TrackFeature.State, TrackFeature.Message, TrackFeature.Action>
        get() = TrackFeatureBuilder.build(
            appGraph.buildTrackDataComponent().trackInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildProgressesDataComponent().progressesInteractor,
            appGraph.buildLearningActivitiesDataComponent().learningActivitiesInteractor,
            appGraph.buildTopicsDataComponent().topicsInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.buildMagicLinksDataComponent().urlPathProcessor,
            gamificationToolbarComponent.gamificationToolbarReducer,
            gamificationToolbarComponent.gamificationToolbarActionDispatcher
        )
}
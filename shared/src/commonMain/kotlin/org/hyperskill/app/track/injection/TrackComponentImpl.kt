package org.hyperskill.app.track.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.track.presentation.TrackFeature
import ru.nobird.app.presentation.redux.feature.Feature

class TrackComponentImpl(private val appGraph: AppGraph) : TrackComponent {
    private val urlPathProcessor: UrlPathProcessor =
        appGraph.buildMagicLinksDataComponent().urlPathProcessor

    override val trackFeature: Feature<TrackFeature.State, TrackFeature.Message, TrackFeature.Action>
        get() = TrackFeatureBuilder.build(
            appGraph.buildTrackDataComponent().trackInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildProgressesDataComponent().progressesInteractor,
            appGraph.buildLearningActivitiesDataComponent().learningActivitiesInteractor,
            appGraph.buildTopicsDataComponent().topicsInteractor,
            appGraph.buildStreaksDataComponent().streaksInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            urlPathProcessor
        )
}
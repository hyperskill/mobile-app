package org.hyperskill.app.track.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.interactor.LearningActivitiesInteractor
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsActionDispatcher
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsFeature
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsReducer
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.topics.domain.interactor.TopicsInteractor
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
        profileInteractor: ProfileInteractor,
        progressesInteractor: ProgressesInteractor,
        learningActivitiesInteractor: LearningActivitiesInteractor,
        topicsInteractor: TopicsInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        urlPathProcessor: UrlPathProcessor,
        navigationBarItemsReducer: NavigationBarItemsReducer,
        navigationBarItemsActionDispatcher: NavigationBarItemsActionDispatcher
    ): Feature<TrackFeature.State, TrackFeature.Message, TrackFeature.Action> {
        val trackReducer = TrackReducer(navigationBarItemsReducer)
        val trackActionDispatcher = TrackActionDispatcher(
            ActionDispatcherOptions(),
            trackInteractor,
            profileInteractor,
            progressesInteractor,
            learningActivitiesInteractor,
            topicsInteractor,
            analyticInteractor,
            sentryInteractor,
            urlPathProcessor
        )

        return ReduxFeature(
            TrackFeature.State(
                trackState = TrackFeature.TrackState.Idle,
                navigationBarItemsState = NavigationBarItemsFeature.State.Idle
            ),
            trackReducer
        )
            .wrapWithActionDispatcher(trackActionDispatcher)
            .wrapWithActionDispatcher(
                navigationBarItemsActionDispatcher.transform(
                    transformAction = { it.safeCast<TrackFeature.Action.NavigationBarItemsAction>()?.action },
                    transformMessage = TrackFeature.Message::NavigationBarItemsMessage
                )
            )
    }
}
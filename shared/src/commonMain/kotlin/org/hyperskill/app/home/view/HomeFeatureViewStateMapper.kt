package org.hyperskill.app.home.view

import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature

object HomeFeatureViewStateMapper {
    fun map(state: HomeFeature.State): HomeFeature.State {
        val topicsState = state.topicsToDiscoverNextState
        return state.copy(
            topicsToDiscoverNextState = if (topicsState is TopicsToDiscoverNextFeature.State.Content) {
                topicsState.copy(topicsToDiscoverNext = topicsState.topicsToDiscoverNext.take(1))
            } else {
                topicsState
            }
        )
    }
}
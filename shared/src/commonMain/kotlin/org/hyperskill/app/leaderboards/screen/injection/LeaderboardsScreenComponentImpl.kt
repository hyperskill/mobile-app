package org.hyperskill.app.leaderboards.screen.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Action
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Message
import org.hyperskill.app.leaderboards.screen.view.model.LeaderboardsScreenViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class LeaderboardsScreenComponentImpl(
    private val appGraph: AppGraph
) : LeaderboardsScreenComponent {

    override val leaderboardsScreenFeature: Feature<LeaderboardsScreenViewState, Message, Action>
        get() = LeaderboardsScreenFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}
package org.hyperskill.app.leaderboards.screen.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Action
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Message
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class LeaderboardsScreenComponentImpl(
    private val appGraph: AppGraph
) : LeaderboardsScreenComponent {
    private val gamificationToolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent(GamificationToolbarScreen.LEADERBOARD)

    override val leaderboardsScreenFeature: Feature<ViewState, Message, Action>
        get() = LeaderboardsScreenFeatureBuilder.build(
            gamificationToolbarReducer = gamificationToolbarComponent.gamificationToolbarReducer,
            gamificationToolbarActionDispatcher = gamificationToolbarComponent.gamificationToolbarActionDispatcher,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}
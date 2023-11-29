package org.hyperskill.app.leaderboard.screen.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Action
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Message
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.ViewState
import org.hyperskill.app.leaderboard.widget.injection.LeaderboardWidgetComponent
import ru.nobird.app.presentation.redux.feature.Feature

internal class LeaderboardScreenComponentImpl(
    private val appGraph: AppGraph
) : LeaderboardScreenComponent {
    private val gamificationToolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent(GamificationToolbarScreen.LEADERBOARD)

    private val leaderboardWidgetComponent: LeaderboardWidgetComponent =
        appGraph.buildLeaderboardWidgetComponent()

    override val leaderboardScreenFeature: Feature<ViewState, Message, Action>
        get() = LeaderboardScreenFeatureBuilder.build(
            leaderWidgetReducer = leaderboardWidgetComponent.leaderboardWidgetReducer,
            leaderboardWidgetActionDispatcher = leaderboardWidgetComponent.leaderboardWidgetActionDispatcher,
            leaderboardWidgetViewStateMapper = leaderboardWidgetComponent.leaderboardWidgetViewStateMapper,
            gamificationToolbarReducer = gamificationToolbarComponent.gamificationToolbarReducer,
            gamificationToolbarActionDispatcher = gamificationToolbarComponent.gamificationToolbarActionDispatcher,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}
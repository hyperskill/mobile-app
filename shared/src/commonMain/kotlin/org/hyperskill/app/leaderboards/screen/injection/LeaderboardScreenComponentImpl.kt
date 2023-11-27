package org.hyperskill.app.leaderboards.screen.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.Action
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.Message
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class LeaderboardScreenComponentImpl(
    private val appGraph: AppGraph
) : LeaderboardScreenComponent {
    private val gamificationToolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent(GamificationToolbarScreen.LEADERBOARD)

    override val leaderboardScreenFeature: Feature<ViewState, Message, Action>
        get() = LeaderboardScreenFeatureBuilder.build(
            gamificationToolbarReducer = gamificationToolbarComponent.gamificationToolbarReducer,
            gamificationToolbarActionDispatcher = gamificationToolbarComponent.gamificationToolbarActionDispatcher,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}
package org.hyperskill.app.leaderboards.screen.injection

import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Action
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Message
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface LeaderboardsScreenComponent {
    val leaderboardsScreenFeature: Feature<ViewState, Message, Action>
}
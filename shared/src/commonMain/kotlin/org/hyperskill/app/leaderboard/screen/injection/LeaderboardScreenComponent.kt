package org.hyperskill.app.leaderboard.screen.injection

import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Action
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Message
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface LeaderboardScreenComponent {
    val leaderboardScreenFeature: Feature<ViewState, Message, Action>
}
package org.hyperskill.app.leaderboard.presentation

import org.hyperskill.app.core.flowredux.presentation.FlowView
import org.hyperskill.app.core.flowredux.presentation.ReduxFlowViewModel
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Action.ViewAction
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Message
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.ViewState
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature

class LeaderboardViewModel(
    viewContainer: FlowView<ViewState, Message, ViewAction>
) : ReduxFlowViewModel<ViewState, Message, ViewAction>(viewContainer) {

    init {
        onNewMessage(Message.Initialize)
    }

    fun onNewMessage(message: GamificationToolbarFeature.Message) {
        onNewMessage(
            Message.GamificationToolbarMessage(message)
        )
    }

    fun onNewMessage(message: LeaderboardWidgetFeature.Message) {
        onNewMessage(
            Message.LeaderboardWidgetMessage(message)
        )
    }
}
package org.hyperskill.app.leaderboard.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.leaderboard.presentation.LeaderboardViewModel
import org.hyperskill.app.leaderboard.screen.injection.LeaderboardScreenComponent

class PlatformLeaderboardComponentImpl(
    private val leaderboardComponent: LeaderboardScreenComponent
) : PlatformLeaderboardComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                LeaderboardViewModel::class.java to {
                    LeaderboardViewModel(
                        leaderboardComponent.leaderboardScreenFeature.wrapWithFlowView()
                    )
                }
            )
        )
}
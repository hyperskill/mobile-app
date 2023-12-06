package org.hyperskill.app.leaderboard.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformLeaderboardComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}
package org.hyperskill.app.android.main.view.ui.navigation

import org.hyperskill.app.android.debug.DebugScreen
import org.hyperskill.app.android.home.view.ui.screen.TrainingScreen
import org.hyperskill.app.android.leaderboard.navigation.LeaderboardScreen
import org.hyperskill.app.android.profile.view.navigation.ProfileScreen
import org.hyperskill.app.android.study_plan.screen.StudyPlanScreen
import ru.nobird.android.view.navigation.router.RetainedRouter

typealias MainScreenRouter = RetainedRouter

fun MainScreenRouter.switch(tab: Tabs) {
    val tabScreen = when (tab) {
        Tabs.TRAINING -> TrainingScreen
        Tabs.STUDY_PLAN -> StudyPlanScreen
        Tabs.LEADERBOARD -> LeaderboardScreen
        Tabs.PROFILE -> ProfileScreen(isInitCurrent = true)
        Tabs.DEBUG -> DebugScreen(isBackNavigationEnabled = false)
    }
    switch(tabScreen)
}
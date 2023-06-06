package org.hyperskill.app.android.main.view.ui.navigation

import org.hyperskill.app.android.debug.DebugScreen
import org.hyperskill.app.android.home.view.ui.screen.HomeScreen
import org.hyperskill.app.android.profile.view.navigation.ProfileScreen
import org.hyperskill.app.android.study_plan.screen.StudyPlanScreen
import ru.nobird.android.view.navigation.router.RetainedRouter

typealias MainScreenRouter = RetainedRouter

fun MainScreenRouter.switch(tab: Tabs) {
    val tabScreen = when (tab) {
        Tabs.HOME -> HomeScreen
        Tabs.STUDY_PLAN -> StudyPlanScreen
        Tabs.PROFILE -> ProfileScreen(isInitCurrent = true)
        Tabs.DEBUG -> DebugScreen
    }
    switch(tabScreen)
}
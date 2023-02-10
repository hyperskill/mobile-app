package org.hyperskill.app.android.core.view.ui.navigation

import ru.nobird.android.view.navigation.router.RetainedRouter

interface MainNavigationContainer  {
    val router: RetainedRouter

    companion object {
        const val ContainerTag: String = "MainNavigationContainer"
    }
}
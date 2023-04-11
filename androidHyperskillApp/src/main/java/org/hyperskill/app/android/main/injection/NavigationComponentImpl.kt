package org.hyperskill.app.android.main.injection

import com.github.terrakok.cicerone.Cicerone
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter

class NavigationComponentImpl : NavigationComponent {

    override val mainScreenCicerone: Cicerone<MainScreenRouter> by lazy {
        Cicerone.create(MainScreenRouter())
    }
}
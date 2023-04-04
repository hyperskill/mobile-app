package org.hyperskill.app.android.main.injection

import com.github.terrakok.cicerone.Cicerone
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter

interface NavigationComponent {
    val mainScreenCicerone: Cicerone<MainScreenRouter>
}
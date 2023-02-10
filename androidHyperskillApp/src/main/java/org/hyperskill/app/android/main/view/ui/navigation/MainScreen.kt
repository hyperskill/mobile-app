package org.hyperskill.app.android.main.view.ui.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.core.view.ui.navigation.MainNavigationContainer
import org.hyperskill.app.android.main.view.ui.fragment.MainFragment

object MainScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        MainFragment.newInstance()

    override val screenKey: String
        get() = MainNavigationContainer.ContainerTag
}
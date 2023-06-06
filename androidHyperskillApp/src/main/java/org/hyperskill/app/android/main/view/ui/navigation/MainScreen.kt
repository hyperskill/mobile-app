package org.hyperskill.app.android.main.view.ui.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.main.view.ui.fragment.MainFragment

/**
 * Represent the main screen with the bottom app bar.
 * @param initialTab means the tab which will be opened during the first screen show
 */
data class MainScreen(private val initialTab: Tabs = Tabs.HOME) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        MainFragment.newInstance(initialTab)
}
package org.hyperskill.app.android.home.view.ui.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.home.view.ui.fragment.HomeFragment
import org.hyperskill.app.android.main.view.ui.navigation.Tabs

object HomeScreen : FragmentScreen {
    override val screenKey: String = Tabs.HOME.name

    override fun createFragment(factory: FragmentFactory): Fragment =
        HomeFragment.newInstance()
}
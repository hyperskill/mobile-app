package org.hyperskill.app.android.profile.view.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.profile.view.fragment.ProfileFragment

object ProfileScreen : FragmentScreen {
    override val screenKey: String = Tabs.PROFILE.name

    override fun createFragment(factory: FragmentFactory): Fragment =
        ProfileFragment.newInstance()
}
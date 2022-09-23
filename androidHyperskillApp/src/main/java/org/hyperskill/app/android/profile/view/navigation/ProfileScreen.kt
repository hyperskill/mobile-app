package org.hyperskill.app.android.profile.view.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.profile.view.fragment.ProfileFragment

class ProfileScreen(private val profileId: Long? = null, private val isInitCurrent: Boolean = true) : FragmentScreen {
    override val screenKey: String = Tabs.PROFILE.name

    override fun createFragment(factory: FragmentFactory): Fragment =
        ProfileFragment.newInstance(profileId, isInitCurrent)
}
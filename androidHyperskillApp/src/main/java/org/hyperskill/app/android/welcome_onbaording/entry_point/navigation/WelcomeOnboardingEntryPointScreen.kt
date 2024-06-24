package org.hyperskill.app.android.welcome_onbaording.entry_point.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.welcome_onbaording.entry_point.fragment.WelcomeOnboardingEntryPointFragment

object WelcomeOnboardingEntryPointScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        WelcomeOnboardingEntryPointFragment.newInstance()
}
package org.hyperskill.app.android.welcome_onbaording.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.welcome_onbaording.fragment.WelcomeOnboardingFragment

object WelcomeOnboardingScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        WelcomeOnboardingFragment.newInstance()
}
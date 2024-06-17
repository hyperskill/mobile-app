package org.hyperskill.app.android.welcome_onbaording.root.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.welcome_onbaording.root.fragment.WelcomeOnboardingFragment
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams

class WelcomeOnboardingScreen(private val params: WelcomeOnboardingFeatureParams) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        WelcomeOnboardingFragment.newInstance(params)
}
package org.hyperskill.app.android.onboarding.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.onboarding.fragment.OnboardingFragment

object OnboardingScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        OnboardingFragment.newInstance()
}
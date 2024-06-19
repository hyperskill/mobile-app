package org.hyperskill.app.android.welcome_onbaording.finish.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.welcome_onbaording.finish.fragment.WelcomeOnboardingFinishFragment
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack

class WelcomeOnboardingFinishScreen(val track: WelcomeOnboardingTrack) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        WelcomeOnboardingFinishFragment.newInstance(track)
}
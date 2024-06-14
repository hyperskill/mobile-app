package org.hyperskill.app.android.welcome_onbaording.track.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.welcome_onbaording.track.fragment.WelcomeOnboardingTrackDetailsFragment
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack

class WelcomeOnboardingTrackDetailsScreen(
    private val track: WelcomeOnboardingTrack
) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        WelcomeOnboardingTrackDetailsFragment.newInstance(track)
}
package org.hyperskill.app.android.notification_onboarding.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.notification_onboarding.fragment.NotificationsOnboardingFragment

object NotificationsOnboardingScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        NotificationsOnboardingFragment.newInstance()
}
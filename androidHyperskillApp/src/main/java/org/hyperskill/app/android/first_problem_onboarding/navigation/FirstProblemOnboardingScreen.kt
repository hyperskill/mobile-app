package org.hyperskill.app.android.first_problem_onboarding.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.first_problem_onboarding.fragment.FirstProblemOnboardingFragment

object FirstProblemOnboardingScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        FirstProblemOnboardingFragment.newInstance()
}
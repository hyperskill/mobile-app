package org.hyperskill.app.android.interview_preparation_onboarding.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.interview_preparation_onboarding.fragement.InterviewPreparationOnboardingFragment
import org.hyperskill.app.step.domain.model.StepRoute

class InterviewPreparationOnboardingScreen(
    private val stepRoute: StepRoute
) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        InterviewPreparationOnboardingFragment.newInstance(stepRoute)
}
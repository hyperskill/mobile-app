package org.hyperskill.app.android.step.view.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.step.view.fragment.StepFragment
import org.hyperskill.app.step.domain.model.StepRoute

class StepScreen(private val stepRoute: StepRoute) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        StepFragment.newInstance(stepRoute)
}
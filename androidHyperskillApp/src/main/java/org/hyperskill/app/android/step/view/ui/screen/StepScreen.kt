package org.hyperskill.app.android.step.view.ui.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.step.view.ui.fragment.StepFragment

object StepScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        StepFragment.newInstance()
}
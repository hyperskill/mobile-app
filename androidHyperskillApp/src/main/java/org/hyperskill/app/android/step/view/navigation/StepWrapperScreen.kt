package org.hyperskill.app.android.step.view.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.step.view.fragment.StepWrapperFragment
import org.hyperskill.app.step.domain.model.StepRoute

@Suppress("DEPRECATION")
@Deprecated("Use it only in the StepFragment")
class StepWrapperScreen(val stepRoute: StepRoute) : FragmentScreen {

    companion object {
        const val TAG = "step_wrapper"
    }

    override val screenKey: String
        get() = TAG

    override fun createFragment(factory: FragmentFactory): Fragment =
        StepWrapperFragment.newInstance(stepRoute)
}
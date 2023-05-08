package org.hyperskill.app.android.study_plan.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.study_plan.fragment.StudyPlanFragment

object StudyPlanScreen : FragmentScreen {
    override val screenKey: String
        get() = Tabs.STUDY_PLAN.name

    override fun createFragment(factory: FragmentFactory): Fragment =
        StudyPlanFragment.newInstance()
}
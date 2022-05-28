package org.hyperskill.app.android.step_quiz.view.ui.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.step_quiz.view.ui.fragment.StepQuizFragment

object StepQuizScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        StepQuizFragment.newInstance()
}

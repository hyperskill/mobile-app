package org.hyperskill.app.android.step_quiz.view.factory

import androidx.fragment.app.Fragment
import org.hyperskill.app.android.step_quiz_sorting.view.fragment.SortingStepQuizFragment
import org.hyperskill.app.android.step_quiz_unsupported.view.fragment.UnsupportedStepQuizFragment
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step

object StepQuizFragmentFactory {
    fun getQuizFragment(step: Step): Fragment =
        when (step.block.name) {
            BlockName.SORTING ->
                SortingStepQuizFragment.newInstance(step)
            else ->
                UnsupportedStepQuizFragment.newInstance()
        }
}
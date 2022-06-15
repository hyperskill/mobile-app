package org.hyperskill.app.android.step_quiz.view.factory

import androidx.fragment.app.Fragment
import org.hyperskill.app.android.step_quiz_matching.view.fragment.MatchingStepQuizFragment
import org.hyperskill.app.android.step_quiz_sorting.view.fragment.SortingStepQuizFragment
import org.hyperskill.app.android.step_quiz_text.view.fragment.TextStepQuizFragment
import org.hyperskill.app.android.step_quiz_unsupported.view.fragment.UnsupportedStepQuizFragment
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step

object StepQuizFragmentFactory {
    fun getQuizFragment(step: Step): Fragment =
        when (step.block.name) {
            BlockName.SORTING ->
                SortingStepQuizFragment.newInstance(step)

            BlockName.NUMBER,
            BlockName.STRING,
            BlockName.MATH ->
                TextStepQuizFragment.newInstance(step)

            BlockName.MATCHING ->
                MatchingStepQuizFragment.newInstance(step)

            else ->
                UnsupportedStepQuizFragment.newInstance()
        }
}
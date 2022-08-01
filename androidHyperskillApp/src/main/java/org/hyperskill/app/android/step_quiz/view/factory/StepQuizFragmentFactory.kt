package org.hyperskill.app.android.step_quiz.view.factory

import androidx.fragment.app.Fragment
import org.hyperskill.app.android.step_quiz_choice.view.fragment.ChoiceStepQuizFragment
import org.hyperskill.app.android.step_quiz_code.view.fragment.CodeStepQuizFragment
import org.hyperskill.app.android.step_quiz_matching.view.fragment.MatchingStepQuizFragment
import org.hyperskill.app.android.step_quiz_sorting.view.fragment.SortingStepQuizFragment
import org.hyperskill.app.android.step_quiz_text.view.fragment.TextStepQuizFragment
import org.hyperskill.app.android.step_quiz_table.view.fragment.TableStepQuizFragment
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

            BlockName.TABLE ->
                TableStepQuizFragment.newInstance(step)

            BlockName.CHOICE ->
                ChoiceStepQuizFragment.newInstance(step)

            BlockName.CODE ->
                CodeStepQuizFragment.newInstance(step)

            else ->
                UnsupportedStepQuizFragment.newInstance()
        }
}
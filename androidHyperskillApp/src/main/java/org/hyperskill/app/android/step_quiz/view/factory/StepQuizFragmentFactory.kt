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
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver

object StepQuizFragmentFactory {
    fun getQuizFragment(step: Step, stepRoute: StepRoute): Fragment =
        if (StepQuizResolver.isQuizSupportable(step.block.name)) {
            when (step.block.name) {
                BlockName.SORTING ->
                    SortingStepQuizFragment.newInstance(step, stepRoute)

                BlockName.NUMBER,
                BlockName.STRING,
                BlockName.MATH ->
                    TextStepQuizFragment.newInstance(step, stepRoute)

                BlockName.MATCHING ->
                    MatchingStepQuizFragment.newInstance(step, stepRoute)

                BlockName.TABLE ->
                    TableStepQuizFragment.newInstance(step, stepRoute)

                BlockName.CHOICE ->
                    ChoiceStepQuizFragment.newInstance(step, stepRoute)

                BlockName.CODE ->
                    CodeStepQuizFragment.newInstance(step, stepRoute)

                else ->
                    UnsupportedStepQuizFragment.newInstance()
            }
        } else {
            UnsupportedStepQuizFragment.newInstance()
        }
}
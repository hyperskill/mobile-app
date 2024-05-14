package org.hyperskill.app.android.step_practice.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step.view.model.StepQuizToolbarCallback
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizFragmentFactory
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute

class StepPracticeFragment : Fragment(R.layout.fragment_step_practice), StepCompletionView, StepQuizToolbarCallback {
    companion object {
        private const val STEP_CONTENT_FRAGMENT_TAG = "step_content"
        private const val STEP_QUIZ_FRAGMENT_TAG = "step_quiz"

        fun newInstance(step: Step, stepRoute: StepRoute): Fragment =
            StepPracticeFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private var step: Step by argument(serializer = Step.serializer())
    private var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStepPracticeDescriptionFragment(step, stepRoute)
        initStepQuizFragment(step, stepRoute)
    }

    private fun initStepPracticeDescriptionFragment(step: Step, stepRoute: StepRoute) {
        setChildFragment(R.id.stepPracticeDescriptionContainer, STEP_CONTENT_FRAGMENT_TAG) {
            StepPracticeDetailsFragment.newInstance(step, stepRoute)
        }
    }

    private fun initStepQuizFragment(step: Step, stepRoute: StepRoute) {
        setChildFragment(R.id.stepQuizContainer, STEP_QUIZ_FRAGMENT_TAG) {
            StepQuizFragmentFactory.getQuizFragment(step, stepRoute)
        }
    }

    override fun renderPracticeLoading(isPracticingLoading: Boolean) {
        (childFragmentManager.findFragmentByTag(STEP_QUIZ_FRAGMENT_TAG) as? StepCompletionView)
            ?.renderPracticeLoading(isPracticingLoading)
    }

    override fun onLimitsClick() {
        (childFragmentManager.findFragmentByTag(STEP_QUIZ_FRAGMENT_TAG) as? StepQuizToolbarCallback)
            ?.onLimitsClick()
    }

    override fun onTheoryClick() {
        (childFragmentManager.findFragmentByTag(STEP_QUIZ_FRAGMENT_TAG) as? StepQuizToolbarCallback)
            ?.onTheoryClick()
    }
}
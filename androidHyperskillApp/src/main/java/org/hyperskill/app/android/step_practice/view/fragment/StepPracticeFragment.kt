package org.hyperskill.app.android.step_practice.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepPracticeBinding
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step.view.model.StepQuizToolbarHost
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizFragmentFactory
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature

class StepPracticeFragment : Fragment(R.layout.fragment_step_practice), StepCompletionView, StepQuizToolbarHost {
    companion object {
        private const val STEP_CONTENT_FRAGMENT_TAG = "step_content"
        private const val STEP_QUIZ_FRAGMENT_TAG = "step_quiz"

        fun newInstance(step: Step, stepRoute: StepRoute): Fragment =
            StepPracticeFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private val viewBinding: FragmentStepPracticeBinding by viewBinding(FragmentStepPracticeBinding::bind)

    private var step: Step by argument(serializer = Step.serializer())
    private var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(viewBinding.stepPracticeAppBar.stepQuizToolbar)
        viewBinding.stepPracticeAppBar.stepQuizToolbar.setNavigationOnClickListener {
            requireRouter().exit()
        }
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

    override fun render(isPracticingLoading: Boolean) {
        (childFragmentManager.findFragmentByTag(STEP_QUIZ_FRAGMENT_TAG) as? StepCompletionView)
            ?.render(isPracticingLoading)
    }

    override fun render(viewState: StepQuizToolbarFeature.ViewState) {
        viewBinding.stepPracticeAppBar.stepQuizLimitsTextView.isVisible =
            viewState is StepQuizToolbarFeature.ViewState.Content.Visible
        if (viewState is StepQuizToolbarFeature.ViewState.Content.Visible) {
            viewBinding.stepPracticeAppBar.stepQuizLimitsTextView.text = viewState.stepsLimitLabel
        }
    }
}
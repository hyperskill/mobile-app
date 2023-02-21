package org.hyperskill.app.android.step_practice.view.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepPracticeBinding
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.step_content_text.view.fragment.TextStepContentFragment
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizFragmentFactory
import org.hyperskill.app.android.step_quiz_hints.fragment.StepQuizHintsFragment
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.view.mapper.StepQuizStatsTextMapper
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature

class StepPracticeFragment : Fragment(R.layout.fragment_step_practice), StepCompletionView {
    companion object {
        private const val STEP_CONTENT_FRAGMENT_TAG = "step_content"
        private const val STEP_QUIZ_FRAGMENT_TAG = "step_quiz"
        private const val STEP_HINTS_FRAGMENT_TAG = "step_hints"

        fun newInstance(step: Step, stepRoute: StepRoute): Fragment =
            StepPracticeFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private var stepQuizStatsTextMapper: StepQuizStatsTextMapper? = null

    private val viewBinding: FragmentStepPracticeBinding by viewBinding(FragmentStepPracticeBinding::bind)

    private var step: Step by argument(serializer = Step.serializer())
    private var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding.stepPracticeAppBar.stepQuizToolbar) {
            root.setNavigationOnClickListener {
                requireRouter().exit()
            }
            root.menu.findItem(R.id.theory).apply {
                isVisible = step.topicTheory != null && stepRoute is StepRoute.Repeat
                actionView?.setOnClickListener {
                    step.topicTheory?.let { theoryId ->
                        requireRouter().navigateTo(StepScreen(StepRoute.Repeat(theoryId)))
                    }
                }
            }
            stepQuizToolbarTitle.text = step.title
        }
        with(viewBinding.stepPracticeCompletion) {
            val stepQuizStats =
                stepQuizStatsTextMapper?.getFormattedStepQuizStats(step.solvedBy, step.millisSinceLastCompleted)
            this.text = stepQuizStats
            isVisible = stepQuizStats != null
        }
        initStepTheoryFragment(step)
        setStepQuizFragment(step, stepRoute)
        setStepHintsFragment(step)
    }

    private fun injectComponent() {
        stepQuizStatsTextMapper = StepQuizStatsTextMapper(HyperskillApp.graph().commonComponent.resourceProvider)
    }

    private fun initStepTheoryFragment(step: Step) {
        setChildFragment(R.id.stepTheoryContainer, STEP_CONTENT_FRAGMENT_TAG) {
            TextStepContentFragment.newInstance(step)
        }
    }

    private fun setStepQuizFragment(step: Step, stepRoute: StepRoute) {
        setChildFragment(R.id.stepQuizContainer, STEP_QUIZ_FRAGMENT_TAG) {
            StepQuizFragmentFactory.getQuizFragment(step, stepRoute)
        }
    }

    private fun setStepHintsFragment(step: Step) {
        val isFeatureEnabled = StepQuizHintsFeature.isHintsFeatureAvailable(step)
        viewBinding.stepQuizHints.isVisible = isFeatureEnabled
        if (isFeatureEnabled) {
            setChildFragment(R.id.stepQuizHints, STEP_HINTS_FRAGMENT_TAG) {
                StepQuizHintsFragment.newInstance(step)
            }
        }
    }

    override fun render(isPracticingLoading: Boolean) {
        (childFragmentManager.findFragmentByTag(STEP_QUIZ_FRAGMENT_TAG) as? StepCompletionView)
            ?.render(isPracticingLoading)
    }
}
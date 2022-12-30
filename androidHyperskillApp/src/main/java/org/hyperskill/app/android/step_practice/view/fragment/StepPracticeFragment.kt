package org.hyperskill.app.android.step_practice.view.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepPracticeBinding
import org.hyperskill.app.android.step_content_text.view.fragment.TextStepContentFragment
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizFragmentFactory
import org.hyperskill.app.android.step_quiz_hints.fragment.StepQuizHintsFragment
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.extension.TimeFancifier
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature

class StepPracticeFragment : Fragment(R.layout.fragment_step_practice) {
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
    private lateinit var resourceProvider: ResourceProvider

    private val viewBinding: FragmentStepPracticeBinding by viewBinding(FragmentStepPracticeBinding::bind)

    private var step: Step by argument(serializer = Step.serializer())
    private var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.stepPracticeAppBar.stepQuizToolbar.root.setNavigationOnClickListener {
            requireRouter().exit()
        }
        viewBinding.stepPracticeAppBar.stepQuizToolbar.stepQuizToolbarTitle.text =
            step.title

        viewBinding.stepPracticeCompletion.text = resourceProvider.getString(
            SharedResources.strings.step_quiz_stats_text,
            step.solvedBy.toString(),
            TimeFancifier.formatTimeDistance(step.millisSinceLastCompleted)
        )
        initStepTheoryFragment(step)
        setStepQuizFragment(step, stepRoute)
        setStepHintsFragment(step)
    }

    private fun injectComponent() {
        resourceProvider = HyperskillApp.graph().commonComponent.resourceProvider
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
}
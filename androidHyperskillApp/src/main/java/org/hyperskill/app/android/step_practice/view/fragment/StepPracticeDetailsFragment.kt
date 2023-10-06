package org.hyperskill.app.android.step_practice.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.databinding.FragmentStepPracticeDescriptionBinding
import org.hyperskill.app.android.step.view.delegate.CollapsibleStepBlockDelegate
import org.hyperskill.app.android.step_content_text.view.delegate.TextStepContentDelegate
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedStepTextDetailsHyperskillAnalyticEvent

class StepPracticeDetailsFragment : Fragment(R.layout.fragment_step_practice_description) {

    companion object {
        fun newInstance(
            step: Step,
            stepRoute: StepRoute
        ): StepPracticeDetailsFragment =
            StepPracticeDetailsFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private var step: Step by argument(Step.serializer())
    private var stepRoute: StepRoute by argument(StepRoute.serializer())

    private val viewBinding: FragmentStepPracticeDescriptionBinding by viewBinding(
        FragmentStepPracticeDescriptionBinding::bind
    )

    private var textStepContentDelegate: TextStepContentDelegate? = null
    private var analyticInteractor: AnalyticInteractor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        textStepContentDelegate = TextStepContentDelegate(lifecycle)
        injectComponent()
    }

    private fun injectComponent() {
        analyticInteractor = HyperskillApp.graph().analyticComponent.analyticInteractor
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textStepContentDelegate?.setup(
            context = requireContext(),
            latexView = viewBinding.stepPracticeDetailsContent.root,
            step = step,
            viewLifecycle = viewLifecycleOwner.lifecycle
        )
        with(viewBinding) {
            stepPracticeDetailsArrow.setIsExpanded(true)
            CollapsibleStepBlockDelegate.setupCollapsibleBlock(
                arrowView = stepPracticeDetailsArrow,
                headerView = stepPracticeDetailsFrameLayout,
                contentView = stepPracticeDetailsContent.root,
                onContentExpandChanged = {
                    lifecycleScope.launch {
                        analyticInteractor?.logEvent(
                            StepQuizClickedStepTextDetailsHyperskillAnalyticEvent(stepRoute.analyticRoute)
                        )
                    }
                }
            )
        }
    }
}
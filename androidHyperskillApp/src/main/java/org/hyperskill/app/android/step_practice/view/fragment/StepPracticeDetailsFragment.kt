package org.hyperskill.app.android.step_practice.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.databinding.FragmentStepPracticeDescriptionBinding
import org.hyperskill.app.android.step_content_text.view.delegate.TextStepContentDelegate
import org.hyperskill.app.android.view.base.ui.extension.collapse
import org.hyperskill.app.android.view.base.ui.extension.expand
import org.hyperskill.app.step.domain.model.Step

class StepPracticeDetailsFragment : Fragment(R.layout.fragment_step_practice_description) {

    companion object {
        fun newInstance(
            step: Step
        ): StepPracticeDetailsFragment =
            StepPracticeDetailsFragment().apply {
                this.step = step
            }
    }

    private var step: Step by argument(Step.serializer())

    private val viewBinding: FragmentStepPracticeDescriptionBinding by viewBinding(
        FragmentStepPracticeDescriptionBinding::bind
    )

    private lateinit var textStepContentDelegate: TextStepContentDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        textStepContentDelegate = TextStepContentDelegate(lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textStepContentDelegate.setup(
            context = requireContext(),
            latexView = viewBinding.stepPracticeDetailsContent.root,
            step = step,
            viewLifecycle = viewLifecycleOwner.lifecycle
        )
        with(viewBinding) {
            stepPracticeDetailsArrow.setIsExpanded(true)
            stepPracticeDetailsFrameLayout.setOnClickListener {
                stepPracticeDetailsArrow.changeState()
                if (stepPracticeDetailsArrow.isExpanded()) {
                    stepPracticeDetailsContent.root.expand()
                } else {
                    stepPracticeDetailsContent.root.collapse()
                }
            }
        }
    }
}
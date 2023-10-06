package org.hyperskill.app.android.step_content_text.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.databinding.StepTextHeaderBinding
import org.hyperskill.app.android.step_content_text.view.delegate.TextStepContentDelegate
import org.hyperskill.app.step.domain.model.Step

class TextStepContentFragment : Fragment(R.layout.step_text_header) {
    companion object {
        fun newInstance(step: Step): TextStepContentFragment =
            TextStepContentFragment().apply {
                this.step = step
            }
    }

    private val viewBinding: StepTextHeaderBinding by viewBinding(StepTextHeaderBinding::bind)

    private var step: Step by argument(Step.serializer())

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
            latexView = viewBinding.root,
            step = step,
            viewLifecycle = viewLifecycleOwner.lifecycle
        )
    }
}
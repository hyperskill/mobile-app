package org.hyperskill.app.android.step_content_text.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chrynan.parcelable.core.getParcelable
import com.chrynan.parcelable.core.putParcelable
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.StepTextHeaderBinding
import org.hyperskill.app.step.domain.model.Step

// TODO Replace with LatexView in ALTAPPS-152
class TextStepContentFragment : Fragment(R.layout.step_text_header) {
    companion object {
        private const val KEY_STEP = "key_step"

        fun newInstance(step: Step): Fragment {
            val arguments = Bundle().apply {
                putParcelable(KEY_STEP, step)
            }
            return TextStepContentFragment().apply {
                this.arguments = arguments
            }
        }
    }

    private val viewBinding: StepTextHeaderBinding by viewBinding(StepTextHeaderBinding::bind)
    private lateinit var step: Step

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        step = requireArguments().getParcelable<Step>(KEY_STEP) ?: throw IllegalStateException()
        viewBinding.root.text = step.block.text
    }
}
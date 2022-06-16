package org.hyperskill.app.android.step_content_text.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chrynan.parcelable.core.getParcelable
import com.chrynan.parcelable.core.putParcelable
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.StepTextHeaderBinding
import org.hyperskill.app.android.latex.view.widget.LatexWebView
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

    private var _binding: StepTextHeaderBinding? = null
    private val viewBinding get() = _binding!!

    private lateinit var step: Step

    private var latexWebView: LatexWebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        step = requireArguments().getParcelable<Step>(KEY_STEP) ?: throw IllegalStateException()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = StepTextHeaderBinding
            .inflate(inflater, container, false)
            .also { stepTextHeaderBinding ->
                if (latexWebView == null) {
                    latexWebView = LayoutInflater
                        .from(requireContext().applicationContext)
                        .inflate(R.layout.layout_latex_webview, stepTextHeaderBinding.root, false) as LatexWebView
                }

                latexWebView?.let {
                    stepTextHeaderBinding.root.addView(it)
                }
            }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.root.setText(step.block.text)
    }

    override fun onDestroyView() {
        viewBinding.root.removeView(latexWebView)
        super.onDestroyView()
    }

    override fun onDestroy() {
        latexWebView = null
        super.onDestroy()
    }
}
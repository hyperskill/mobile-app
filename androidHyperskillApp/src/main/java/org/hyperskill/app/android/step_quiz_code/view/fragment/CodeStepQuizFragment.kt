package org.hyperskill.app.android.step_quiz_code.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.chrynan.parcelable.core.putParcelable
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizCodeBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeLayoutDelegate
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeQuizInstructionDelegate
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeStepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_fullscreen_code.dialog.CodeStepQuizFullScreenDialogFragment
import org.hyperskill.app.android.step_quiz_fullscreen_code.dialog.ResetCodeDialogFragment
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.app.presentation.redux.container.ReduxView
import ru.nobird.android.view.base.ui.extension.showIfNotExists

class CodeStepQuizFragment :
    DefaultStepQuizFragment(),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction>,
    CodeStepQuizFullScreenDialogFragment.Callback,
    ResetCodeDialogFragment.Callback {
    companion object {
        fun newInstance(step: Step): Fragment {
            val arguments = Bundle().apply {
                putParcelable(KEY_STEP, step)
            }
            return CodeStepQuizFragment().apply {
                this.arguments = arguments
            }
        }
    }

    private var _binding: LayoutStepQuizCodeBinding? = null
    private val binding get() = _binding!!

    private lateinit var codeOptions: Block.Options

    private lateinit var codeStepQuizFormDelegate: CodeStepQuizFormDelegate
    private lateinit var codeLayoutDelegate: CodeLayoutDelegate

    private lateinit var lang: String

    override val quizViews: Array<View>
        get() = arrayOf(binding.stepQuizCodeContainer)

    override val skeletonView: View
        get() = binding.stepQuizCodeSkeleton.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = LayoutStepQuizCodeBinding.inflate(LayoutInflater.from(requireContext()), viewBinding.root, false)
        viewBinding.root.addView(binding.root)

        viewBinding.stepQuizButtons.stepQuizRetryButton.setOnClickListener {
            val dialog = ResetCodeDialogFragment.newInstance()
            if (!dialog.isAdded) {
                dialog.show(childFragmentManager, null)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

    override fun createStepQuizFormDelegate(containerBinding: FragmentStepQuizBinding): StepQuizFormDelegate {
        codeOptions = step.block.options
        lang = codeOptions.limits!!.keys.first()

        val codeDetailsView = (viewBinding.root.parent.parent as View).findViewById<View>(R.id.stepQuizCodeSamples)
        codeDetailsView.isVisible = true

        codeLayoutDelegate = CodeLayoutDelegate(
            codeLayout = binding.codeStepLayout,
            step = step,
            codeTemplates = codeOptions.codeTemplates!!,
            codeQuizInstructionDelegate = CodeQuizInstructionDelegate(
                codeDetailsView,
                true,
                onDetailsIsExpandedStateChanged = {
                    logAnalyticEventMessage(StepQuizFeature.Message.StepQuizClickedCodeDetailsEventMessage)
                }
            ),
            codeToolbarAdapter = null,
        )

        codeStepQuizFormDelegate = CodeStepQuizFormDelegate(
            containerBinding = containerBinding,
            codeLayout = binding.codeStepLayout,
            lang = lang,
            code = codeOptions.codeTemplates?.get(lang) ?: "",
            codeLayoutDelegate = codeLayoutDelegate,
            onFullscreenClicked = ::onFullScreenClicked,
            onQuizChanged = ::syncReplyState
        )

        return codeStepQuizFormDelegate
    }

    override fun onSyncCodeStateWithParent(code: String, onSubmitClicked: Boolean) {
        codeStepQuizFormDelegate.updateCodeLayoutFromDialog(code)
        if (onSubmitClicked) {
            onActionButtonClicked()
        }
    }

    private fun onFullScreenClicked(lang: String, code: String) {
        CodeStepQuizFullScreenDialogFragment
            .newInstance(
                lang,
                code,
                codeOptions.codeTemplates!!,
                step,
                viewBinding.stepQuizButtons.stepQuizRetryButton.isVisible
            )
            .showIfNotExists(childFragmentManager, CodeStepQuizFullScreenDialogFragment.TAG)
    }

    override fun onReset() {
        codeStepQuizFormDelegate.updateCodeLayoutFromDialog(codeOptions.codeTemplates?.get(lang) ?: "")
    }
}
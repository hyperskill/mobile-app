package org.hyperskill.app.android.step_quiz_code.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizCodeBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeLayoutDelegate
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeQuizInstructionDelegate
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeStepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_fullscreen_code.dialog.CodeStepQuizFullScreenDialogFragment
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.presentation.redux.container.ReduxView

class CodeStepQuizFragment :
    DefaultStepQuizFragment(),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction>,
    CodeStepQuizFullScreenDialogFragment.Callback {

    companion object {
        fun newInstance(step: Step, stepRoute: StepRoute): Fragment =
            CodeStepQuizFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private var _binding: LayoutStepQuizCodeBinding? = null
    private val binding get() = _binding!!

    private lateinit var codeOptions: Block.Options

    private var codeStepQuizFormDelegate: CodeStepQuizFormDelegate? = null

    private lateinit var langName: String

    override val quizViews: Array<View>
        get() = arrayOf(binding.stepQuizCodeContainer)

    override val skeletonView: View
        get() = binding.stepQuizCodeSkeleton.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = LayoutStepQuizCodeBinding.inflate(LayoutInflater.from(requireContext()), viewBinding.root, false)
        viewBinding.root.addView(binding.root)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        codeStepQuizFormDelegate = null
        super.onDestroyView()
    }

    override fun createStepQuizFormDelegate(containerBinding: FragmentStepQuizBinding): StepQuizFormDelegate {
        codeOptions = step.block.options
        langName = codeOptions.limits!!.keys.first()

        val codeDetailsView = (viewBinding.root.parent.parent as View).findViewById<View>(R.id.stepQuizCodeSamples)
        codeDetailsView.isVisible = true

        val codeLayoutDelegate = CodeLayoutDelegate(
            codeLayout = binding.codeStepLayout,
            step = step,
            codeTemplates = codeOptions.codeTemplates!!,
            codeQuizInstructionDelegate = CodeQuizInstructionDelegate(
                codeDetailsView,
                true,
                onDetailsIsExpandedStateChanged = {
                    logAnalyticEventMessage(StepQuizFeature.Message.ClickedCodeDetailsEventMessage)
                }
            ),
            codeToolbarAdapter = null
        )

        val codeStepQuizFormDelegate = CodeStepQuizFormDelegate(
            containerBinding = containerBinding,
            codeLayout = binding.codeStepLayout,
            langName = langName,
            initialCode = codeOptions.codeTemplates?.get(langName) ?: "",
            codeLayoutDelegate = codeLayoutDelegate,
            onFullscreenClicked = ::onFullScreenClicked,
            onQuizChanged = ::syncReplyState
        )
        this.codeStepQuizFormDelegate = codeStepQuizFormDelegate

        return codeStepQuizFormDelegate
    }

    override fun onNewState(state: StepQuizFeature.State) {
        if (state is StepQuizFeature.State.AttemptLoaded) {
            val submission = (state.submissionState as? StepQuizFeature.SubmissionState.Loaded)
                ?.submission
            val replyCode = submission?.reply?.code
            val fullScreenFragment = childFragmentManager
                .findFragmentByTag(CodeStepQuizFullScreenDialogFragment.TAG) as? CodeStepQuizFullScreenDialogFragment
            fullScreenFragment?.onNewCode(replyCode)
        }
    }

    override fun onSyncCodeStateWithParent(code: String, onSubmitClicked: Boolean) {
        codeStepQuizFormDelegate?.updateCodeLayoutFromDialog(code, onSubmitClicked)
        if (onSubmitClicked) {
            onSubmitButtonClicked()
        }
    }

    override fun onResetCodeClick() {
        onRetryButtonClicked()
    }

    private fun onFullScreenClicked(lang: String, code: String?) {
        CodeStepQuizFullScreenDialogFragment
            .newInstance(
                CodeStepQuizFullScreenDialogFragment.Params(
                    lang = lang,
                    code = code ?: codeOptions.codeTemplates?.get(lang) ?: "",
                    codeTemplates = codeOptions.codeTemplates!!,
                    step = step,
                    isShowRetryButton = viewBinding.stepQuizButtons.stepQuizRetryLogoOnlyButton.isVisible
                )
            )
            .showIfNotExists(childFragmentManager, CodeStepQuizFullScreenDialogFragment.TAG)
    }
}
package org.hyperskill.app.android.step_quiz_code.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.util.CodeEditorKeyboardExtensionUtil
import org.hyperskill.app.android.code.view.adapter.CodeToolbarAdapter
import org.hyperskill.app.android.databinding.LayoutStepQuizCodeBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizCodeKeyboardExtensionBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeLayoutDelegate
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeQuizInstructionDelegate
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeStepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_code.view.model.CodeStepQuizConfigFactory
import org.hyperskill.app.android.step_quiz_code.view.model.config.CodeStepQuizConfig
import org.hyperskill.app.android.step_quiz_fullscreen_code.dialog.CodeStepQuizFullScreenDialogFragment
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
        fun newInstance(
            step: Step,
            stepRoute: StepRoute
        ): Fragment =
            CodeStepQuizFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private var _binding: LayoutStepQuizCodeBinding? = null
    private val binding get() = _binding!!

    private val config: CodeStepQuizConfig by lazy(LazyThreadSafetyMode.NONE) {
        CodeStepQuizConfigFactory.create(step)
    }

    private var codeStepQuizFormDelegate: CodeStepQuizFormDelegate? = null
    private var codeToolbarAdapter: CodeToolbarAdapter? = null

    override val quizViews: Array<View>
        get() = arrayOf(binding.stepQuizCodeContainer)

    override val skeletonView: View
        get() = binding.stepQuizCodeSkeleton.root

    override val descriptionBinding: LayoutStepQuizDescriptionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeToolbarAdapter = CodeToolbarAdapter(requireContext())
    }

    override fun createStepView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
        val binding = LayoutStepQuizCodeBinding.inflate(layoutInflater, parent, false).also {
            _binding = it
        }
        setupKeyboardExtensionView(layoutInflater, binding)
        return binding.root
    }

    private fun setupKeyboardExtensionView(layoutInflater: LayoutInflater, viewBinding: LayoutStepQuizCodeBinding) {
        val parentFragmentViewGroup = requireParentFragment().requireView() as ViewGroup
        val keyboardExtensionViewBinding = LayoutStepQuizCodeKeyboardExtensionBinding.inflate(
            layoutInflater,
            parentFragmentViewGroup,
            false
        )
        parentFragmentViewGroup.addView(keyboardExtensionViewBinding.root)

        CodeEditorKeyboardExtensionUtil.setupKeyboardExtension(
            context = requireContext(),
            rootView = parentFragmentViewGroup,
            recyclerView = keyboardExtensionViewBinding.stepQuizCodeKeyboardExtensionRecycler,
            codeLayout = viewBinding.stepQuizCodeEmbeddedEditor.codeStepLayout,
            codeToolbarAdapter = requireNotNull(codeToolbarAdapter),
            codeEditorKeyboardListener = { isKeyboardShown, toolbarHeight ->
                if (isResumed) {
                    onKeyboardStateChanged(isKeyboardShown)
                    viewBinding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        bottomMargin = if (isKeyboardShown) {
                            toolbarHeight
                        } else {
                            context?.resources
                                ?.getDimensionPixelOffset(R.dimen.step_quiz_content_vertical_padding) ?: 0
                        }
                    }
                }
            }
        )
    }

    override fun onDestroyView() {
        _binding = null
        codeStepQuizFormDelegate = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        codeToolbarAdapter = null
    }

    override fun createStepQuizFormDelegate(): StepQuizFormDelegate {
        val codeStepQuizFormDelegate = CodeStepQuizFormDelegate(
            viewBinding = binding.stepQuizCodeEmbeddedEditor,
            codeLayoutDelegate = createCodeLayoutDelegate(),
            codeStepQuizConfig = config,
            onFullscreenClicked = ::onFullScreenClicked,
            onQuizChanged = ::syncReplyState
        )
        this.codeStepQuizFormDelegate = codeStepQuizFormDelegate

        return codeStepQuizFormDelegate
    }

    private fun createCodeLayoutDelegate(): CodeLayoutDelegate =
        CodeLayoutDelegate(
            codeLayout = binding.stepQuizCodeEmbeddedEditor.codeStepLayout,
            config = config,
            codeQuizInstructionDelegate = CodeQuizInstructionDelegate(
                binding.codeStepSamples.root,
                true,
                onDetailsIsExpandedStateChanged = {
                    logAnalyticEventMessage(StepQuizFeature.Message.ClickedCodeDetailsEventMessage)
                }
            ),
            codeToolbarAdapter = codeToolbarAdapter
        )

    override fun onNewState(state: StepQuizFeature.State) {
        (state.stepQuizState as? StepQuizFeature.StepQuizState.AttemptLoaded)?.let { attemptLoadedState ->
            val submission = (attemptLoadedState.submissionState as? StepQuizFeature.SubmissionState.Loaded)
                ?.submission
            val replyCode = config.getCode(submission)
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

    private fun onFullScreenClicked(lang: String, code: String) {
        CodeStepQuizFullScreenDialogFragment
            .newInstance(
                CodeStepQuizFullScreenDialogFragment.Params(
                    lang = lang,
                    code = code,
                    step = step,
                    isShowRetryButton = viewBinding.stepQuizButtons.stepQuizRetryLogoOnlyButton.isVisible
                )
            )
            .showIfNotExists(childFragmentManager, CodeStepQuizFullScreenDialogFragment.TAG)
    }
}
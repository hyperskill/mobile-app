package org.hyperskill.app.android.step_quiz_code.view.delegate

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.button.MaterialButton
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.view.widget.withoutTextChangeCallback
import org.hyperskill.app.android.databinding.LayoutEmbeddedCodeEditorBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_code.view.model.config.CodeStepQuizConfig
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver

class CodeStepQuizFormDelegate(
    private val viewBinding: LayoutEmbeddedCodeEditorBinding,
    private val codeLayoutDelegate: CodeLayoutDelegate,
    private val codeStepQuizConfig: CodeStepQuizConfig,
    private val onFullscreenClicked: (lang: String, code: String) -> Unit,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private var code: String? = codeStepQuizConfig.initialCode
    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            code = p0?.toString()
            onQuizChanged(codeStepQuizConfig.createReply(code))
        }
    }

    init {
        with(codeLayoutDelegate) {
            setEnabled(true)
            setLanguage(codeStepQuizConfig.langName, code)
            setDetailsContentData(codeStepQuizConfig.langName)
        }
        with(viewBinding) {
            embeddedCodeEditorExpand.setOnClickListener {
                onFullscreenClicked(
                    codeStepQuizConfig.langName,
                    this@CodeStepQuizFormDelegate.code ?: codeStepQuizConfig.initialCode
                )
            }
            codeStepLayout.codeEditor.minLines = 5
            codeStepLayout.codeEditor.addTextChangedListener(textWatcher)
        }
    }

    override fun customizeSubmissionButton(button: MaterialButton) {
        with(button) {
            setText(org.hyperskill.app.R.string.step_quiz_code_run_solution_button_text)
            setIconResource(R.drawable.ic_run)
            iconPadding =
                context.resources.getDimensionPixelSize(
                    R.dimen.step_quiz_fullscreen_code_layout_action_button_icon_padding
                )
        }
    }

    override fun createReply(): Reply =
        codeStepQuizConfig.createReply(code)

    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        val submission = (state.submissionState as? StepQuizFeature.SubmissionState.Loaded)
            ?.submission
        val replyCode = codeStepQuizConfig.getCode(submission)
        this.code = replyCode

        val isEnabled = StepQuizResolver.isQuizEnabled(state)
        codeLayoutDelegate.setEnabled(isEnabled)

        viewBinding.codeStepLayout.withoutTextChangeCallback(textWatcher) {
            codeLayoutDelegate.setLanguage(codeStepQuizConfig.langName, replyCode)
            codeLayoutDelegate.setDetailsContentData(codeStepQuizConfig.langName)
        }
    }

    fun updateCodeLayoutFromDialog(newCode: String, onSubmitClicked: Boolean) {
        this.code = newCode
        if (onSubmitClicked) {
            viewBinding.codeStepLayout.withoutTextChangeCallback(textWatcher) {
                codeLayoutDelegate.setLanguage(codeStepQuizConfig.langName, code)
                codeLayoutDelegate.setDetailsContentData(codeStepQuizConfig.langName)
            }
        } else {
            codeLayoutDelegate.setLanguage(codeStepQuizConfig.langName, code)
            codeLayoutDelegate.setDetailsContentData(codeStepQuizConfig.langName)
        }
    }
}
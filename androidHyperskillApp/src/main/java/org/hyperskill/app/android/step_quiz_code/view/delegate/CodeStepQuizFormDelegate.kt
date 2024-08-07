package org.hyperskill.app.android.step_quiz_code.view.delegate

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isInvisible
import com.google.android.material.button.MaterialButton
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.view.widget.withoutTextChangeCallback
import org.hyperskill.app.android.databinding.LayoutEmbeddedCodeEditorBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizCodeBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_code.view.model.config.CodeStepQuizConfig
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.reply
import org.hyperskill.app.submissions.domain.model.Reply

class CodeStepQuizFormDelegate(
    private val context: Context,
    private val viewBinding: LayoutStepQuizCodeBinding,
    private val codeLayoutDelegate: CodeLayoutDelegate,
    private val codeStepQuizConfig: CodeStepQuizConfig,
    private val callback: Callback
) : StepQuizFormDelegate {

    interface Callback {
        fun onFullscreenClicked(lang: String, code: String)

        fun onQuizChanged(reply: Reply)
    }

    private val codeEditorViewBinding: LayoutEmbeddedCodeEditorBinding =
        viewBinding.stepQuizCodeEmbeddedEditor

    private var code: String? = codeStepQuizConfig.initialCode
    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            code = p0?.toString()
            callback.onQuizChanged(codeStepQuizConfig.createReply(code))
        }
    }

    init {
        with(codeLayoutDelegate) {
            setLanguage(codeStepQuizConfig.langName, code)
            setDetailsContentData(codeStepQuizConfig.langName)
        }
        with(codeEditorViewBinding) {
            embeddedCodeEditorExpand.setOnClickListener {
                callback.onFullscreenClicked(
                    codeStepQuizConfig.langName,
                    this@CodeStepQuizFormDelegate.code ?: codeStepQuizConfig.initialCode
                )
            }
            val displayedLangName = codeStepQuizConfig.displayedLangName
            embeddedCodeEditorLanguageTextView.isInvisible = displayedLangName == null
            if (displayedLangName != null) {
                embeddedCodeEditorLanguageTextView.text = context.resources.getString(
                    org.hyperskill.app.R.string.step_quiz_code_language_template,
                    displayedLangName
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
        val replyCode = codeStepQuizConfig.getCode(state.submissionState.reply)
        this.code = replyCode

        val isEnabled = StepQuizResolver.isQuizEnabled(state)
        codeLayoutDelegate.setEnabled(isEnabled)
        codeEditorViewBinding.embeddedCodeEditorExpand.isEnabled = isEnabled

        codeEditorViewBinding.codeStepLayout.withoutTextChangeCallback(textWatcher) {
            codeLayoutDelegate.setLanguage(codeStepQuizConfig.langName, replyCode)
            codeLayoutDelegate.setDetailsContentData(codeStepQuizConfig.langName)
        }
    }

    fun updateCodeLayoutFromDialog(newCode: String, onSubmitClicked: Boolean) {
        this.code = newCode
        if (onSubmitClicked) {
            codeEditorViewBinding.codeStepLayout.withoutTextChangeCallback(textWatcher) {
                codeLayoutDelegate.setLanguage(codeStepQuizConfig.langName, code)
                codeLayoutDelegate.setDetailsContentData(codeStepQuizConfig.langName)
            }
        } else {
            codeLayoutDelegate.setLanguage(codeStepQuizConfig.langName, code)
            codeLayoutDelegate.setDetailsContentData(codeStepQuizConfig.langName)
        }
    }
}
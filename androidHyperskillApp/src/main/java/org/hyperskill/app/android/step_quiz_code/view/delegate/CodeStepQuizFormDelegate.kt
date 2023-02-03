package org.hyperskill.app.android.step_quiz_code.view.delegate

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
import org.hyperskill.app.android.code.view.widget.withoutTextChangeCallback
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver

class CodeStepQuizFormDelegate(
    containerBinding: FragmentStepQuizBinding,
    private val codeLayout: CodeEditorLayout,
    initialCode: String,
    private val lang: String,
    private val codeLayoutDelegate: CodeLayoutDelegate,
    private val onFullscreenClicked: (lang: String, code: String?) -> Unit,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private var code: String? = initialCode
    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            code = p0?.toString()
            onQuizChanged(createReply())
        }
    }

    init {
        (
            (containerBinding.root.parent.parent as View).findViewById<View>(R.id.stepQuizDescriptionDivider)
                .layoutParams as ViewGroup.MarginLayoutParams
            )
            .setMargins(0, 0, 0, 0)

        containerBinding.stepQuizDescription.setText(org.hyperskill.app.R.string.step_quiz_code_write_program_text)

        with(containerBinding.stepQuizButtons.stepQuizSubmitButton) {
            setText(org.hyperskill.app.R.string.step_quiz_code_run_solution_button_text)
            setIconResource(R.drawable.ic_run)
            iconPadding = context.resources.getDimensionPixelSize(R.dimen.step_quiz_fullscreen_code_layout_action_button_icon_padding)
        }

        with(codeLayoutDelegate) {
            setEnabled(true)
            setLanguage(lang, code)
            setDetailsContentData(lang)
        }

        with(codeLayout.codeEditor) {
            isFocusable = false
            addTextChangedListener(textWatcher)
            codeLayout.codeEditor.setOnClickListener {
                onFullscreenClicked(lang, code)
            }
        }
    }

    override fun createReply(): Reply =
        Reply(code = code, language = lang)

    override fun setState(state: StepQuizFeature.State.AttemptLoaded) {
        val submission = (state.submissionState as? StepQuizFeature.SubmissionState.Loaded)
            ?.submission
        val replyCode = submission?.reply?.code
        this.code = replyCode

        val isEnabled = StepQuizResolver.isQuizEnabled(state)
        codeLayoutDelegate.setEnabled(isEnabled)

        codeLayout.withoutTextChangeCallback(textWatcher) {
            codeLayoutDelegate.setLanguage(lang, replyCode)
            codeLayoutDelegate.setDetailsContentData(lang)
        }
    }

    fun updateCodeLayoutFromDialog(newCode: String, onSubmitClicked: Boolean) {
        this.code = newCode
        if (onSubmitClicked) {
            codeLayout.withoutTextChangeCallback(textWatcher) {
                codeLayoutDelegate.setLanguage(lang, code)
                codeLayoutDelegate.setDetailsContentData(lang)
            }
        } else {
            codeLayoutDelegate.setLanguage(lang, code)
            codeLayoutDelegate.setDetailsContentData(lang)
        }
    }
}
package org.hyperskill.app.android.step_quiz_code.view.delegate

import android.view.View
import androidx.core.widget.doAfterTextChanged
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.model.ReplyResult
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver

class CodeStepQuizFormDelegate(
    private val containerBinding: FragmentStepQuizBinding,
    private val codeLayout: CodeEditorLayout,
    private val codeOptions: Block.Options,
    private val codeLayoutDelegate: CodeLayoutDelegate,
    private val onFullscreenClicked: (lang: String, code: String) -> Unit,
    private val onQuizChanged: (ReplyResult) -> Unit
) : StepQuizFormDelegate {
    private val quizDescription = containerBinding.stepQuizDescription

    private val lang: String = codeOptions.limits!!.keys.first()
    private var code: String = codeOptions.codeTemplates?.get(lang) ?: ""

    init {
        quizDescription.setText(R.string.step_quiz_code_write_program_text)
        quizDescription.visibility = View.GONE

        with(containerBinding.stepQuizButtons.stepQuizSubmitButton) {
            setText(R.string.step_quiz_code_run_solution_button_text)
            setIconResource(R.drawable.ic_run)
            iconPadding = context.resources.getDimensionPixelSize(R.dimen.step_quiz_fullscreen_code_layout_action_button_icon_padding)
        }

//        (containerBinding.root.parent.parent as View).findViewById<View>(R.id.stepQuizDescriptionDivider).visibility = View.GONE

        with(codeLayout.codeEditor) {
            isFocusable = false

            setOnClickListener {
                onFullscreenClicked(lang, code)
            }

            doAfterTextChanged {
                onQuizChanged(createReply())
            }
        }

        codeLayoutDelegate.setEnabled(true)

        updateCodeLayoutFromDialog(code)
    }

    override fun createReply(): ReplyResult =
        ReplyResult(Reply(code = code, language = lang), ReplyResult.Validation.Success)

    override fun setState(state: StepQuizFeature.State.AttemptLoaded) {
        val isEnabled = StepQuizResolver.isQuizEnabled(state)
        codeLayoutDelegate.setEnabled(isEnabled)
    }

    fun updateCodeLayoutFromDialog(newCode: String) {
        code = newCode
        codeLayoutDelegate.setLanguage(lang, code)
        codeLayoutDelegate.setDetailsContentData(lang)
    }
}
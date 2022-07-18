package org.hyperskill.app.android.step_quiz_code.view.delegate

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.view.model.themes.CodeTheme
import org.hyperskill.app.android.code.view.model.themes.Presets
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizCodeBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.model.ReplyResult
import org.hyperskill.app.android.step_quiz_code.view.mapper.CodeStepQuizFormStateMapper
import org.hyperskill.app.android.step_quiz_code.view.model.CodeStepQuizFormState
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate

class CodeStepQuizFormDelegate(
    private val containerBinding: FragmentStepQuizBinding,
    private val codeLayout: CodeEditorLayout,
    private val codeOptions: Block.Options,
    private val codeLayoutDelegate: CodeLayoutDelegate,
    private val onFullscreenClicked: (lang: String, code: String) -> Unit,
    private val onQuizChanged: (ReplyResult) -> Unit
) : StepQuizFormDelegate {
    private var state: CodeStepQuizFormState = CodeStepQuizFormState.Idle
        set(value) {
            field = value

            viewStateDelegate.switchState(value)

            when (value) {
                is CodeStepQuizFormState.Lang ->
                    codeLayoutDelegate.setLanguage(value.lang, value.code)
            }
            codeLayoutDelegate.setDetailsContentData((value as? CodeStepQuizFormState.Lang)?.lang)
        }

    private val quizDescription = containerBinding.stepQuizDescription
    private val viewStateDelegate = ViewStateDelegate<CodeStepQuizFormState>()

    private val lang: String = codeOptions.limits!!.keys.first()
    private var code: String = codeOptions.codeTemplates?.get(lang) ?: ""

    private val codeStepQuizFormStateMapper = CodeStepQuizFormStateMapper()

    init {
        viewStateDelegate.addState<CodeStepQuizFormState.Idle>()
        viewStateDelegate.addState<CodeStepQuizFormState.Lang>(codeLayout)

        quizDescription.setText(R.string.step_quiz_code_write_program_text)
        quizDescription.visibility = View.GONE

        containerBinding.stepQuizSubmitButton.setText(R.string.step_quiz_code_run_solution_button_text)

//        (containerBinding.root.parent.parent as View).findViewById<View>(R.id.stepQuizDescriptionDivider).visibility = View.GONE

        codeLayout.codeEditor.isFocusable = false
        codeLayout.codeEditor.setOnClickListener {
//            val oldState = (state as? CodeStepQuizFormState.Lang)
//                ?: return@setOnClickListener
            onFullscreenClicked(lang, code)
        }
        codeLayout.codeEditor.doAfterTextChanged {
            if (state is CodeStepQuizFormState.Idle) return@doAfterTextChanged
            onQuizChanged(createReply())
        }

        codeLayoutDelegate.setEnabled(true)

        updateCodeLayoutFromDialog(code)
    }

    override fun createReply(): ReplyResult {
        return ReplyResult(Reply(code = code, language = lang), ReplyResult.Validation.Success)
//        val state = state
//        return if (state is CodeStepQuizFormState.Lang) {
//            ReplyResult(Reply(code = state.code, language = state.lang), ReplyResult.Validation.Success)
//        } else {
//            ReplyResult(Reply(), ReplyResult.Validation.Error(codeLayout.context.getString(R.string.step_quiz_code_empty_lang)))
//        }
    }

    override fun setState(state: StepQuizFeature.State.AttemptLoaded) {

//        this.state = codeStepQuizFormStateMapper.mapToFormState(codeOptions, state)

//        val isEnabled = StepQuizResolver.isQuizEnabled(state)
//        codeLayoutDelegate.setEnabled(isEnabled)
    }

    fun updateCodeLayoutFromDialog(newCode: String) {
        code = newCode
        codeLayoutDelegate.setLanguage(lang, code)
        codeLayoutDelegate.setDetailsContentData(lang)
    }
}
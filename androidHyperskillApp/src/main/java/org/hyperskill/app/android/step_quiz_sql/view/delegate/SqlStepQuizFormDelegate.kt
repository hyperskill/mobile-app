package org.hyperskill.app.android.step_quiz_sql.view.delegate

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.button.MaterialButton
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.presentation.model.ProgrammingLanguage
import org.hyperskill.app.android.code.presentation.model.extensionForLanguage
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
import org.hyperskill.app.android.code.view.widget.withoutTextChangeCallback
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import ru.nobird.app.core.model.safeCast

class SqlStepQuizFormDelegate(
    private val sqlCodeTemplate: String?,
    containerBinding: FragmentStepQuizBinding,
    private val codeLayout: CodeEditorLayout,
    private val onFullscreenClicked: (lang: String, code: String?) -> Unit,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private var code: String? = sqlCodeTemplate

    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            code = p0?.toString()
            onQuizChanged(createReply())
        }
    }

    init {
        containerBinding.stepQuizDescription.setText(org.hyperskill.app.R.string.step_quiz_sql_title)

        with(codeLayout.codeEditor) {
            isFocusable = false

            setOnClickListener {
                onFullscreenClicked(ProgrammingLanguage.SQL.serverPrintableName, code)
            }
            addTextChangedListener(textWatcher)
        }

        with(codeLayout) {
            isEnabled = true
            langExtension = extensionForLanguage(ProgrammingLanguage.SQL.serverPrintableName)
        }
    }

    override fun customizeSubmissionButton(button: MaterialButton) {
        with(button) {
            setText(org.hyperskill.app.R.string.step_quiz_code_run_solution_button_text)
            setIconResource(R.drawable.ic_run)
            iconPadding =
                context.resources.getDimensionPixelSize(R.dimen.step_quiz_fullscreen_code_layout_action_button_icon_padding)
        }
    }

    override fun createReply(): Reply =
        Reply.sql(code)

    override fun setState(state: StepQuizFeature.State.AttemptLoaded) {
        val submission = state.submissionState.safeCast<StepQuizFeature.SubmissionState.Loaded>()?.submission
        val replyCode = submission?.reply?.solveSql
        this.code = replyCode

        val isEnabled = StepQuizResolver.isQuizEnabled(state)
        with(codeLayout) {
            setEnabled(isEnabled)
            withoutTextChangeCallback(textWatcher) {
                setTextIfChanged(replyCode ?: sqlCodeTemplate ?: "")
            }
        }
    }

    fun updateCodeLayoutFromDialog(newCode: String, onSubmitClicked: Boolean) {
        this.code = newCode
        if (onSubmitClicked) {
            codeLayout.withoutTextChangeCallback(textWatcher) { editor ->
                editor.setTextIfChanged(newCode)
            }
        } else {
            codeLayout.setTextIfChanged(newCode)
        }
    }
}
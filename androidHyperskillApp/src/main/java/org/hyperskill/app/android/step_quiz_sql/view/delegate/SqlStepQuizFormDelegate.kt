package org.hyperskill.app.android.step_quiz_sql.view.delegate

import android.text.TextWatcher
import androidx.core.widget.doAfterTextChanged
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.presentation.model.ProgrammingLanguage
import org.hyperskill.app.android.code.presentation.model.extensionForLanguage
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
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
    private var textWatcher: TextWatcher? = null

    init {
        containerBinding.stepQuizDescription.setText(org.hyperskill.app.R.string.step_quiz_sql_title)
        with(containerBinding.stepQuizButtons.stepQuizSubmitButton) {
            setText(org.hyperskill.app.R.string.step_quiz_code_run_solution_button_text)
            setIconResource(R.drawable.ic_run)
            iconPadding =
                context.resources.getDimensionPixelSize(R.dimen.step_quiz_fullscreen_code_layout_action_button_icon_padding)
        }

        with(codeLayout.codeEditor) {
            isFocusable = false

            setOnClickListener {
                onFullscreenClicked(ProgrammingLanguage.SQL.serverPrintableName, code)
            }
        }

        with(codeLayout) {
            isEnabled = true
            lang = extensionForLanguage(ProgrammingLanguage.SQL.serverPrintableName)
        }
    }

    override fun createReply(): Reply =
        Reply.sql(code)

    override fun setState(state: StepQuizFeature.State.AttemptLoaded) {
        val submission = state.submissionState.safeCast<StepQuizFeature.SubmissionState.Loaded>()?.submission
        val replyCode = submission?.reply?.solveSql
        code = replyCode

        val isEnabled = StepQuizResolver.isQuizEnabled(state)
        with(codeLayout) {
            setEnabled(isEnabled)
            setTextIfChanged(code ?: sqlCodeTemplate ?: "")

            /**
             * Set textWatcher only after initial text set to avoid premature [onQuizChanged] call
             * */
            if (textWatcher == null) {
                textWatcher = codeEditor.doAfterTextChanged {
                    onQuizChanged(createReply())
                }
            }
        }
    }

    fun updateCodeLayoutFromDialog(newCode: String) {
        this.code = newCode
        codeLayout.setTextIfChanged(newCode)
    }
}
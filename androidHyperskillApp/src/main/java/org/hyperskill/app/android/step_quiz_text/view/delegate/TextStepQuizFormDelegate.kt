package org.hyperskill.app.android.step_quiz_text.view.delegate

import android.text.InputType
import android.text.InputType.TYPE_NUMBER_FLAG_SIGNED
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import org.hyperskill.app.android.databinding.LayoutStepQuizTextBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class TextStepQuizFormDelegate(
    binding: LayoutStepQuizTextBinding,
    private val stepBlockName: String?,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {
    private val quizTextField = binding.stringStepQuizFieldEditText as TextView
    init {
        quizTextField.inputType =
            when (val blockName = stepBlockName) {
                BlockName.STRING ->
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

                BlockName.NUMBER ->
                    InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_DECIMAL or
                        TYPE_NUMBER_FLAG_SIGNED

                BlockName.MATH ->
                    InputType.TYPE_CLASS_TEXT

                else -> unsupportedBlockError(blockName)
            }

        quizTextField.doAfterTextChanged {
            onQuizChanged(createReply())
        }
    }

    override fun createReply(): Reply =
        quizTextField.text.toString().let { value ->
            when (stepBlockName) {
                BlockName.NUMBER ->
                    Reply(number = value)
                BlockName.MATH ->
                    Reply(formula = value)
                else ->
                    Reply(text = value, files = emptyList())
            }
        }

    override fun setState(state: StepQuizFeature.State.AttemptLoaded) {
        val submission = (state.submissionState as? StepQuizFeature.SubmissionState.Loaded)
            ?.submission

        val reply = submission?.reply
        val text =
            when (stepBlockName) {
                BlockName.NUMBER ->
                    reply?.number

                BlockName.MATH ->
                    reply?.formula

                else ->
                    reply?.text
            } ?: ""

        quizTextField.isEnabled = StepQuizResolver.isQuizEnabled(state)
        quizTextField.setTextIfChanged(text)
    }

    private fun unsupportedBlockError(blockName: String?): Nothing =
        error("Unsupported block type = $blockName")
}
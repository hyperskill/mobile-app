package org.hyperskill.app.android.step_quiz_text.view.delegate

import android.text.InputType
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizTextBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class TextStepQuizFormDelegate(
    containerBinding: FragmentStepQuizBinding,
    binding: LayoutStepQuizTextBinding,
    private val stepBlockName: String?,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {
    private val quizTextField = binding.stringStepQuizFieldEditText as TextView
    private val quizDescription = containerBinding.stepQuizDescription

    init {
        val (inputType, @StringRes descriptionTextRes) =
            when (val blockName = stepBlockName) {
                BlockName.STRING ->
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE to R.string.step_quiz_string_title

                BlockName.NUMBER ->
                    InputType.TYPE_CLASS_NUMBER to R.string.step_quiz_number_title

                BlockName.MATH ->
                    InputType.TYPE_CLASS_TEXT to R.string.step_quiz_math_title

                else ->
                    throw IllegalArgumentException("Unsupported block type = $blockName")
            }

        quizTextField.inputType = inputType
        quizDescription.setText(descriptionTextRes)
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

        quizTextField.isEnabled = submission?.isSubmissionEditable ?: true
        quizTextField.setTextIfChanged(text)
    }
}
package org.hyperskill.app.android.step_quiz_text.view.delegate

import android.text.InputType
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizTextBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.model.ReplyResult
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class TextStepQuizFormDelegate(
    containerBinding: FragmentStepQuizBinding,
    binding: LayoutStepQuizTextBinding,
    private val stepBlockName: String?,
    private val onQuizChanged: (ReplyResult) -> Unit
) : StepQuizFormDelegate {
    companion object {
        private const val MINUS = "-\\\u002D\u00AD\u2012\u2013\u2014\u2015\u02D7"
        private const val PLUS = "+"
        private const val POINT = ",\\."
        private const val EXP = "eEеЕ"

        private const val NUMBER_VALIDATION_REGEX = "^[$MINUS$PLUS]?[0-9]*[$POINT]?[0-9]+([$EXP][$$MINUS$PLUS]?[0-9]+)?$"
    }

    private val context = containerBinding.root.context

    private val quizTextField = binding.stringStepQuizFieldEditText as TextView
    private val quizDescription = containerBinding.stepQuizDescription

    init {
        val (inputType, @StringRes descriptionTextRes) =
            when (val blockName = stepBlockName) {
                BlockName.STRING ->
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE to R.string.step_quiz_string_description

                BlockName.NUMBER ->
                    InputType.TYPE_CLASS_NUMBER to R.string.step_quiz_number_description  // TODO Ask design team for number task description

                BlockName.MATH ->
                    InputType.TYPE_CLASS_TEXT to R.string.step_quiz_math_description    // TODO Ask design team for math task description

                else ->
                    throw IllegalArgumentException("Unsupported block type = $blockName")
            }

        quizTextField.inputType = inputType
        quizDescription.setText(descriptionTextRes)
        quizTextField.doAfterTextChanged {
            onQuizChanged(createReply())
        }
    }

    override fun createReply(): ReplyResult =
        quizTextField.text.toString().let { value ->
            if (value.isNotEmpty()) {
                when (stepBlockName) {
                    BlockName.NUMBER ->
                        if (value.matches(NUMBER_VALIDATION_REGEX.toRegex())) {
                            ReplyResult((Reply(number = value)), ReplyResult.Validation.Success)
                        } else {
                            ReplyResult(Reply(number = value), ReplyResult.Validation.Error(context.getString(R.string.step_quiz_text_invalid_number_reply)))
                        }

                    BlockName.MATH ->
                        ReplyResult((Reply(formula = value)), ReplyResult.Validation.Success)

                    else ->
                        ReplyResult((Reply(text = value)), ReplyResult.Validation.Success)
                }
            } else {
                ReplyResult(Reply(), ReplyResult.Validation.Error(context.getString(R.string.step_quiz_text_empty_reply)))
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
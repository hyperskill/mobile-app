package org.hyperskill.app.android.step_quiz_text.view.delegate

import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import org.hyperskill.app.android.databinding.LayoutStepQuizTextBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_text.view.model.TextStepQuizConfig
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.reply
import org.hyperskill.app.submissions.domain.model.Reply
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class TextStepQuizFormDelegate(
    private val viewBinding: LayoutStepQuizTextBinding,
    private val config: TextStepQuizConfig,
    private val onQuizChanged: (Reply) -> Unit,
    private val onMarkAsCorrectQuestionClick: () -> Unit
) : StepQuizFormDelegate {

    init {
        with(viewBinding.stringStepQuizFieldEditText) {
            inputType = config.inputType
            setHint(config.getTextFieldHint())
            doAfterTextChanged {
                onQuizChanged(createReply())
            }
        }
        viewBinding.stringStepQuizMarkAsCorrectQuestionIcon.setOnClickListener {
            onMarkAsCorrectQuestionClick()
        }
        viewBinding.stringStepQuizMarkAsCorrectCheckBox.setOnCheckedChangeListener { _, _ ->
            onQuizChanged(createReply())
        }
    }

    override fun createReply(): Reply =
        viewBinding.stringStepQuizFieldEditText.text.toString().let { text ->
            config.createReply(
                inputText = text,
                markedAsCorrect = viewBinding.stringStepQuizMarkAsCorrectCheckBox.isChecked
            )
        }

    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        val reply = state.submissionState.reply
        with(viewBinding.stringStepQuizFieldEditText) {
            isEnabled = StepQuizResolver.isQuizEnabled(state)

            val text = reply?.let(config::getText)
            setTextIfChanged(text ?: "")
        }
        val markedAsCorrectCheckBoxState = config.getMarkedAsCorrectCheckBoxState(reply)
        viewBinding.stringStepQuizMarkAsCorrectContainer.isVisible = markedAsCorrectCheckBoxState != null
        with(viewBinding.stringStepQuizMarkAsCorrectCheckBox) {
            if (markedAsCorrectCheckBoxState != null && isChecked) {
                isChecked = markedAsCorrectCheckBoxState.isChecked
            }
        }
    }
}
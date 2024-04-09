package org.hyperskill.app.android.step_quiz_text.view.delegate

import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_text.view.model.TextStepQuizConfig
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.reply
import org.hyperskill.app.submissions.domain.model.Reply
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class TextStepQuizFormDelegate(
    private val quizTextField: TextView,
    private val config: TextStepQuizConfig,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    init {
        with(quizTextField) {
            inputType = config.inputType
            setHint(config.getTextFieldHint())
            doAfterTextChanged {
                onQuizChanged(createReply())
            }
        }
    }

    override fun createReply(): Reply =
        quizTextField.text.toString().let(config::createReply)

    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        quizTextField.isEnabled = StepQuizResolver.isQuizEnabled(state)

        val text = state.submissionState.reply?.let(config::getText)
        quizTextField.setTextIfChanged(text ?: "")
    }
}
package org.hyperskill.app.android.step_quiz_text.view.model

import android.text.InputType
import org.hyperskill.app.submissions.domain.model.Reply

object MathStepQuizConfig : TextStepQuizConfig {
    override val inputType: Int
        get() = InputType.TYPE_CLASS_TEXT

    override fun createReply(inputText: String, markedAsCorrect: Boolean): Reply =
        Reply(formula = inputText)

    override fun getText(reply: Reply): String? =
        reply.formula
}
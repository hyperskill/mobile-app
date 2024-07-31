package org.hyperskill.app.android.step_quiz_text.view.model

import android.text.InputType
import org.hyperskill.app.submissions.domain.model.Reply

object NumberStepQuizConfig : TextStepQuizConfig {
    override val inputType: Int
        get() = InputType.TYPE_CLASS_NUMBER or
            InputType.TYPE_NUMBER_FLAG_DECIMAL or
            InputType.TYPE_NUMBER_FLAG_SIGNED

    override fun createReply(inputText: String, markedAsCorrect: Boolean): Reply =
        Reply.number(number = inputText)

    override fun getText(reply: Reply): String? =
        reply.number
}
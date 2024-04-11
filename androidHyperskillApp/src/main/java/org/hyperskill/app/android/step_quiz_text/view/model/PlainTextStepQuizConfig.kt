package org.hyperskill.app.android.step_quiz_text.view.model

import android.text.InputType
import org.hyperskill.app.submissions.domain.model.Reply

object PlainTextStepQuizConfig : TextStepQuizConfig {
    override val inputType: Int
        get() = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

    override fun createReply(inputText: String, markedAsCorrect: Boolean): Reply =
        Reply(text = inputText, files = emptyList())

    override fun getText(reply: Reply): String? =
        reply.text
}
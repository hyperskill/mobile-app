package org.hyperskill.app.android.step_quiz_text.view.model

import android.text.InputType
import org.hyperskill.app.R
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.isPromptForceScoreCheckboxChecked

object AIPromptStepQuizConfig : TextStepQuizConfig {
    override val inputType: Int
        get() = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

    override fun getTextFieldHint(): Int =
        R.string.step_quiz_prompt_text_field_hint

    override fun getMarkedAsCorrectCheckBoxState(reply: Reply?): MarkedAsCorrectCheckBoxState =
        MarkedAsCorrectCheckBoxState(isChecked = reply?.isPromptForceScoreCheckboxChecked() ?: false)

    override fun createReply(inputText: String, markedAsCorrect: Boolean): Reply =
        Reply.prompt(prompt = inputText, markedAsCorrect = markedAsCorrect)

    override fun getText(reply: Reply): String? =
        reply.prompt
}
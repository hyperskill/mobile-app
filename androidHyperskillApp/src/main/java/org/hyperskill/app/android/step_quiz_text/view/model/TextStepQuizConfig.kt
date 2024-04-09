package org.hyperskill.app.android.step_quiz_text.view.model

import androidx.annotation.StringRes
import org.hyperskill.app.R
import org.hyperskill.app.submissions.domain.model.Reply

interface TextStepQuizConfig {
    val inputType: Int

    @StringRes
    fun getTextFieldHint(): Int =
        R.string.step_quiz_text_field_hint

    fun createReply(inputText: String): Reply

    fun getText(reply: Reply): String?
}
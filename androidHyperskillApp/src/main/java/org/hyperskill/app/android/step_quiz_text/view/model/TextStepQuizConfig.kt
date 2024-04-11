package org.hyperskill.app.android.step_quiz_text.view.model

import androidx.annotation.StringRes
import org.hyperskill.app.R
import org.hyperskill.app.submissions.domain.model.Reply

interface TextStepQuizConfig {
    val inputType: Int

    @StringRes
    fun getTextFieldHint(): Int =
        R.string.step_quiz_text_field_hint

    fun getMarkedAsCorrectCheckBoxState(reply: Reply?): MarkedAsCorrectCheckBoxState? =
        null

    fun createReply(inputText: String, markedAsCorrect: Boolean): Reply

    fun getText(reply: Reply): String?
}

data class MarkedAsCorrectCheckBoxState(val isChecked: Boolean)
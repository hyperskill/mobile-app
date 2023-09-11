package org.hyperskill.app.android.step_quiz_parsons.view.mapper

import android.text.Spannable
import androidx.core.text.HtmlCompat
import org.hyperskill.app.android.step_quiz_parsons.view.model.UiParsonsLine
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission

object ParsonsLinesMapper {
    fun mapToParsonsLines(
        step: Step,
        attempt: Attempt,
        submission: Submission?,
        selectedLinePosition: Int?,
        isEnabled: Boolean
    ): List<UiParsonsLine> {
        val linesDataset = attempt.dataset?.lines
        return if (submission == null) {
            linesDataset?.mapIndexed { index, text ->
                UiParsonsLine(
                    lineNumber = index,
                    formattedText = parseLineText(text),
                    originText = text,
                    langName = step.block.options.language ?: "",
                    tabsCount = 0,
                    isSelected = isEnabled && index == selectedLinePosition,
                    isClickable = isEnabled
                )
            } ?: emptyList()
        } else {
            val replyLines = submission.reply?.lines
            replyLines?.mapIndexed { index, replyLine ->
                val text =
                    linesDataset?.getOrNull(replyLine.lineNumber) ?: ""
                UiParsonsLine(
                    lineNumber = replyLine.lineNumber,
                    formattedText = parseLineText(text),
                    originText = text,
                    langName = step.block.options.language ?: "",
                    tabsCount = replyLine.level,
                    isSelected = isEnabled && index == selectedLinePosition,
                    isClickable = isEnabled
                )
            } ?: emptyList()
        }
    }

    /**
     * Html parsing is used to handle symbols like `&gt`
     */
    private fun parseLineText(text: String): Spannable =
        HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT) as Spannable
}
package org.hyperskill.app.android.step_quiz_parsons.view.mapper

import android.text.Spanned
import androidx.core.text.HtmlCompat
import org.hyperskill.app.android.step_quiz_parsons.view.model.ParsonsLine
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission

object ParsonsLinesMapper {
    fun mapToParsonsLines(
        attempt: Attempt,
        submission: Submission?,
        selectedLinePosition: Int?
    ): List<ParsonsLine> {
        val linesDataset = attempt.dataset?.lines
        return if (submission == null) {
            linesDataset?.mapIndexed { index, text ->
                ParsonsLine(
                    lineNumber = index,
                    text = parseLineText(text),
                    tabsCount = 0,
                    isSelected = index == selectedLinePosition
                )
            } ?: emptyList()

        } else {
            val replyLines = submission.reply?.lines
            replyLines?.mapIndexed { index, replyLine ->
                val text =
                    linesDataset?.getOrNull(replyLine.lineNumber) ?: ""
                ParsonsLine(
                    lineNumber = replyLine.lineNumber,
                    text = parseLineText(text),
                    tabsCount = replyLine.level,
                    isSelected = index == selectedLinePosition
                )
            } ?: emptyList()
        }
    }

    /**
     * Html parsing is used to handle symbols like `&gt`
     */
    private fun parseLineText(text: String): Spanned =
        HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
}
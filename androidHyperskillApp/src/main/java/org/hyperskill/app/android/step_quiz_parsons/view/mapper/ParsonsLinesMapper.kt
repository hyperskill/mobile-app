package org.hyperskill.app.android.step_quiz_parsons.view.mapper

import android.text.Spannable
import androidx.core.text.HtmlCompat
import org.hyperskill.app.android.code.presentation.highlight.prettify.PrettifyParser
import org.hyperskill.app.android.code.presentation.model.extensionForLanguage
import org.hyperskill.app.android.code.view.applyPrettifyParseResults
import org.hyperskill.app.android.code.view.model.themes.CodeTheme
import org.hyperskill.app.android.step_quiz_parsons.view.model.UiParsonsLine
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission

class ParsonsLinesMapper(
    private val codeTheme: CodeTheme
) {

    private val prettifyParser: PrettifyParser = PrettifyParser()
    private val linesCache: MutableMap<Int, Spannable> = mutableMapOf()

    fun mapToParsonsLines(
        step: Step,
        attempt: Attempt,
        submission: Submission?,
        selectedLinePosition: Int?,
        isEnabled: Boolean
    ): List<UiParsonsLine> {
        val linesDataset = attempt.dataset?.lines
        val langName = step.block.options.language ?: ""
        return if (submission == null) {
            linesDataset?.mapIndexed { index, text ->
                UiParsonsLine(
                    lineNumber = index,
                    formattedText = getFormattedText(
                        lineNumber = index,
                        text = text,
                        langName = langName,
                        codeTheme = codeTheme,
                        linesCache = linesCache
                    ),
                    originText = text,
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
                    formattedText = getFormattedText(
                        lineNumber = replyLine.lineNumber,
                        text = text,
                        langName = langName,
                        codeTheme = codeTheme,
                        linesCache = linesCache
                    ),
                    originText = text,
                    tabsCount = replyLine.level,
                    isSelected = isEnabled && index == selectedLinePosition,
                    isClickable = isEnabled
                )
            } ?: emptyList()
        }
    }

    private fun getFormattedText(
        lineNumber: Int,
        text: String,
        langName: String,
        codeTheme: CodeTheme,
        linesCache: MutableMap<Int, Spannable>
    ): Spannable {
        val cached = linesCache[lineNumber]
        return if (cached != null) {
            cached
        } else {
            val spannable = parseLineText(text, langName, codeTheme)
            linesCache[lineNumber] = spannable
            spannable
        }
    }

    /**
     * Parse input text from Html and add programming language highlighting
     */
    private fun parseLineText(
        text: String,
        langName: String,
        codeTheme: CodeTheme
    ): Spannable {
        val htmlSpannable = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT) as Spannable
        val parsedHtmlString = htmlSpannable.toString()
        val prettifyParseResult = prettifyParser.parse(
            /* fileExtension = */ extensionForLanguage(langName),
            /* content = */ parsedHtmlString
        )
        return htmlSpannable.applyPrettifyParseResults(
            prettifyParseResults = prettifyParseResult,
            start = 0,
            end = parsedHtmlString.lastIndex,
            theme = codeTheme
        )
    }
}
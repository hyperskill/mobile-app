package org.hyperskill.app.step_quiz_fill_blanks.presentation

import org.hyperskill.app.core.utils.DotMatchesAllRegexOption
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.attempts.Component
import org.hyperskill.app.step_quiz.domain.model.attempts.Dataset
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksConfig
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksData
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import ru.nobird.app.core.model.slice

object FillBlanksItemMapper {
    private const val LINE_BREAK_CHAR = '\n'
    private val DELIMITERS = charArrayOf(LINE_BREAK_CHAR, FillBlanksConfig.BLANK_FIELD_CHAR)

    private const val LANGUAGE_CLASS_PREFIX = "class=\"language-"
    private val contentRegex: Regex =
        "<pre><code(.*?)>(.*?)</code></pre>".toRegex(DotMatchesAllRegexOption)

    fun map(attempt: Attempt, submission: Submission?): FillBlanksData? =
        attempt.dataset?.components?.let {
            map(
                componentsDataset = it,
                replyBlanks = submission?.reply?.blanks
            )
        }

    fun map(dataset: Dataset, reply: Reply?): FillBlanksData? =
        dataset.components?.let {
            map(
                componentsDataset = it,
                replyBlanks = reply?.blanks
            )
        }

    private fun map(
        componentsDataset: List<Component>,
        replyBlanks: List<String>?
    ): FillBlanksData? {
        val textComponent = componentsDataset.first()
        val rawText = textComponent.text ?: return null

        val match = contentRegex.find(rawText)
        return if (match != null) {
            val inputComponents = componentsDataset.slice(from = 1)
            val (langClass, content) = match.destructured
            val fillBlanks = splitContent(content) { id, inputIndex ->
                FillBlanksItem.Input(
                    id = id,
                    inputText = replyBlanks?.getOrNull(inputIndex)
                        ?: inputComponents.getOrNull(inputIndex)?.text,
                )
            }
            FillBlanksData(fillBlanks, parseLanguage(langClass))
        } else {
            null
        }
    }

    private fun parseLanguage(langClass: String): String? =
        langClass
            .trimIndent()
            .removeSurrounding(LANGUAGE_CLASS_PREFIX, "\"")
            .takeIf { it.isNotEmpty() }

    private fun splitContent(
        content: String,
        produceInputItem: (id: Int, inputIndex: Int) -> FillBlanksItem.Input
    ): List<FillBlanksItem> {
        var nextDelimiterIndex = content.indexOfAny(DELIMITERS)
        if (nextDelimiterIndex == -1) {
            return listOf(
                FillBlanksItem.Text(
                    id = 0,
                    text = content,
                    startsWithNewLine = false
                )
            )
        }
        return buildList {
            var currentOffset = 0
            var previousDelimiterIsLineBreak = false
            var inputIndex = 0
            var id = 0
            do {
                add(
                    FillBlanksItem.Text(
                        id = id++,
                        text = content.substring(currentOffset, nextDelimiterIndex),
                        startsWithNewLine = previousDelimiterIsLineBreak
                    )
                )

                val delimiter = content[nextDelimiterIndex]
                if (delimiter == FillBlanksConfig.BLANK_FIELD_CHAR) {
                    add(
                        produceInputItem(id++, inputIndex++)
                    )
                }

                previousDelimiterIsLineBreak = delimiter == LINE_BREAK_CHAR
                currentOffset = nextDelimiterIndex + 1 // skip delimiter, start with the next index
                nextDelimiterIndex = content.indexOfAny(DELIMITERS, currentOffset)
            } while(nextDelimiterIndex != -1)
            add(
               FillBlanksItem.Text(
                   id = id,
                   text = content.substring(currentOffset, content.length),
                   startsWithNewLine = previousDelimiterIsLineBreak
               )
            )
        }
    }
}
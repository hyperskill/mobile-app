package org.hyperskill.app.step_quiz_fill_blanks.presentation

import org.hyperskill.app.core.utils.DotMatchesAllRegexOption
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.attempts.Component
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksConfig
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksData
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import ru.nobird.app.core.model.slice

object FillBlanksItemMapper {
    private const val LANGUAGE_CLASS_PREFIX = "class=\"language-"
    private val contentRegex: Regex =
        "<pre><code(.*?)>(.*?)</code></pre>".toRegex(DotMatchesAllRegexOption)

    fun map(attempt: Attempt, submission: Submission): FillBlanksData? =
        attempt.dataset?.components?.let {
            mapInternal(
                componentsDataset = it,
                replyBlanks = submission.reply?.blanks
            )
        }

    internal fun mapInternal(
        componentsDataset: List<Component>,
        replyBlanks: List<String>?
    ): FillBlanksData? {
        val textComponent = componentsDataset.first()
        val rawText = textComponent.text ?: return null

        val match = contentRegex.find(rawText)
        return if (match != null) {
            val (langClass, content) = match.destructured

            val fillBlanks = buildList {
                val blanksIndices = content.allIndicesOf(FillBlanksConfig.BLANK_FIELD_CHAR)
                val inputComponent = componentsDataset.slice(from = 1)
                var startIndex = 0
                blanksIndices.forEachIndexed { blankIndex, blankIndexInString ->
                    add(
                        FillBlanksItem.Text(
                            text = content.substring(startIndex = startIndex, endIndex = blankIndexInString)
                        )
                    )
                    startIndex = blankIndexInString + 1
                    add(
                        FillBlanksItem.Input(
                            inputText = replyBlanks?.getOrNull(blankIndex)
                                ?: inputComponent.getOrNull(blankIndex)?.text
                        )
                    )
                }
                if (startIndex <= content.lastIndex) {
                    add(
                        FillBlanksItem.Text(
                            text = content.substring(startIndex = startIndex)
                        )
                    )
                }
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

    private fun String.allIndicesOf(char: Char): List<Int> =
        mapIndexedNotNullTo(mutableListOf()) { index, c ->
            if (c == char) {
                index
            } else {
                null
            }
        }
}
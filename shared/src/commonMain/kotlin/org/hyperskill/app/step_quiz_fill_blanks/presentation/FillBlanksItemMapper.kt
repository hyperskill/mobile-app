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
import ru.nobird.app.core.model.mutate
import ru.nobird.app.core.model.slice

class FillBlanksItemMapper {
    companion object {
        private const val LINE_BREAK_CHAR = '\n'
        private const val LANGUAGE_CLASS_PREFIX = "class=\"language-"
        private const val WHITE_SPACE_HTML_STRING = "&nbsp;"

        private val DELIMITERS = charArrayOf(LINE_BREAK_CHAR, FillBlanksConfig.BLANK_FIELD_CHAR)
        private val contentRegex: Regex =
            "<pre><code(.*?)>(.*?)</code></pre>".toRegex(DotMatchesAllRegexOption)
    }

    private var cachedLanguage: String? = null
    private var cachedItems: List<FillBlanksItem> = emptyList()
    private var inputItemIndices: List<Int> = emptyList()

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

    internal fun map(
        componentsDataset: List<Component>,
        replyBlanks: List<String>?
    ): FillBlanksData? {
        if (cachedItems.isNotEmpty()) {
            return FillBlanksData(
                fillBlanks = getCachedItems(cachedItems, componentsDataset, replyBlanks),
                language = cachedLanguage
            )
        }

        val textComponent = componentsDataset.first()
        val rawText = textComponent.text ?: return null

        val match = contentRegex.find(rawText)
        return if (match != null) {
            val (langClass, content) = match.destructured
            val inputComponents = componentsDataset.slice(from = 1)
            val fillBlanksItems = splitContent(content) { id, inputIndex ->
                FillBlanksItem.Input(
                    id = id,
                    inputText = getInputText(replyBlanks, inputComponents, inputIndex),
                )
            }
            val language = parseLanguage(langClass)
            this.cachedItems = fillBlanksItems
            this.cachedLanguage = langClass
            this.inputItemIndices = fillBlanksItems.mapIndexedNotNull { index, fillBlanksItem ->
                if (fillBlanksItem is FillBlanksItem.Input) index else null
            }
            this.cachedLanguage = language
            FillBlanksData(fillBlanksItems, language)
        } else {
            null
        }
    }

    private fun getCachedItems(
        cachedItems: List<FillBlanksItem>,
        components: List<Component>,
        replyBlanks: List<String>?
    ): List<FillBlanksItem> =
        cachedItems.mutate {
            inputItemIndices.forEachIndexed { inputIndex, itemIndex ->
                set(
                    itemIndex,
                    FillBlanksItem.Input(
                        id = itemIndex,
                        inputText = getInputText(
                            replyBlanks = replyBlanks,
                            inputComponents = components.slice(from = 1),
                            inputIndex = inputIndex
                        )
                    )
                )
            }
        }

    private fun getInputText(
        replyBlanks: List<String>?,
        inputComponents: List<Component>,
        inputIndex: Int
    ): String? =
        replyBlanks?.getOrNull(inputIndex)
            ?: inputComponents.getOrNull(inputIndex)?.text

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
                getTextItem(
                    id = 0,
                    text = content,
                    startsWithNewLine = false,
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
                    getTextItem(
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
            } while (nextDelimiterIndex != -1)
            add(
                getTextItem(
                    id = id,
                    text = content.substring(currentOffset, content.length),
                    startsWithNewLine = previousDelimiterIsLineBreak
                )
            )
        }
    }

    private fun getTextItem(
        id: Int,
        text: String,
        startsWithNewLine: Boolean
    ): FillBlanksItem.Text {
        val startWhiteSpacesAmount = text
            .indexOfFirst { !it.isWhitespace() }
            .let { if (it == -1) text.length else it }
        return FillBlanksItem.Text(
            id = id,
            text = buildString {
                repeat(startWhiteSpacesAmount) {
                    append(WHITE_SPACE_HTML_STRING)
                }
                append(text.trimStart())
            },
            startsWithNewLine = startsWithNewLine
        )
    }
}
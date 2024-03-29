package org.hyperskill.app.step_quiz_fill_blanks.presentation

import org.hyperskill.app.core.utils.DotMatchesAllRegexOption
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.attempts.Component
import org.hyperskill.app.step_quiz.domain.model.attempts.Dataset
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksConfig
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksData
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksOption
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksItemMapper.Companion.WHITE_SPACE_HTML_STRING
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission
import ru.nobird.app.core.model.mutate
import ru.nobird.app.core.model.slice

/**
 * Extracts content from <pre><code>...</code><pre> HTML tags.
 * Extracts the language name from the <code class="language-...">, if present.
 *
 * Maps content into a list of [FillBlanksItem],
 * replacing the '▭' with [FillBlanksItem.Input], and the rest of the text with [FillBlanksItem.Text].
 *
 * Splits the text by '\n'.
 * It is necessary to make text items smaller, and not occupy the whole width of the widget.
 * This way, there will be enough space for input items,
 * so that they will be placed next to the text item, not on the next line.
 *
 * Replace the leading whitespaces with the [WHITE_SPACE_HTML_STRING] so that HTML parser keeps them as is.
 *
 * Fill the [FillBlanksItem.Input] with the data from [Reply].blanks or [Component].text with the type == "input".
 */
class FillBlanksItemMapper(private val mode: FillBlanksMode) {
    companion object {
        private const val LINE_BREAK_CHAR = '\n'
        private const val LANGUAGE_CLASS_PREFIX = "class=\"language-"
        private const val WHITE_SPACE_HTML_STRING = "&nbsp;"

        private val DELIMITERS = charArrayOf(LINE_BREAK_CHAR, FillBlanksConfig.BLANK_FIELD_CHAR)
        private val contentRegex: Regex =
            "<pre><code(.*?)>(.*?)</code></pre>".toRegex(DotMatchesAllRegexOption)
        private val blankOptionRegex: Regex =
            "<code>(.*?)</code>".toRegex(DotMatchesAllRegexOption)
    }

    private var cachedLanguage: String? = null
    private var cachedItems: List<FillBlanksItem> = emptyList()
    private var inputItemIndices: List<Int> = emptyList()
    private var selectItemIndices: List<Int> = emptyList()
    private var cachedOptions: List<FillBlanksOption> = emptyList()

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
                language = cachedLanguage,
                options = cachedOptions
            )
        }

        val textComponent = componentsDataset.first()
        val rawText = textComponent.text ?: return null

        val (langClass, content) = parseRawText(rawText)
        val blanksComponents = componentsDataset.slice(from = 1)

        val blankOptions = getBlankOptions(blanksComponents)

        val fillBlanksItems = splitContent(
            content = content,
            produceInputItem = { id, inputIndex ->
                FillBlanksItem.Input(
                    id = id,
                    inputText = getInputText(replyBlanks, blanksComponents, inputIndex),
                )
            },
            produceSelectItem = { id, optionIndex ->
                FillBlanksItem.Select(
                    id = id,
                    selectedOptionIndex = getSelectedOptionIndex(replyBlanks, blankOptions, optionIndex)
                )
            }
        )

        val language = langClass?.let(::parseLanguage)

        this.cachedItems = fillBlanksItems
        when (mode) {
            FillBlanksMode.INPUT -> {
                this.inputItemIndices = fillBlanksItems.mapIndexedNotNull { index, fillBlanksItem ->
                    if (fillBlanksItem is FillBlanksItem.Input) index else null
                }
            }
            FillBlanksMode.SELECT -> {
                this.selectItemIndices = fillBlanksItems.mapIndexedNotNull { index, fillBlanksItem ->
                    if (fillBlanksItem is FillBlanksItem.Select) index else null
                }
            }
        }
        this.cachedLanguage = language
        this.cachedOptions = blankOptions

        return FillBlanksData(
            fillBlanks = fillBlanksItems,
            language = language,
            options = blankOptions
        )
    }

    private fun parseRawText(rawText: String): Pair<String?, String> {
        val match = contentRegex.find(rawText)
        return if (match != null) {
            val (langClass, content) = match.destructured
            Pair(langClass, content)
        } else {
            Pair(null, rawText)
        }
    }

    private fun getCachedItems(
        cachedItems: List<FillBlanksItem>,
        components: List<Component>,
        replyBlanks: List<String>?
    ): List<FillBlanksItem> =
        cachedItems.mutate {
            when (mode) {
                FillBlanksMode.INPUT -> {
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
                FillBlanksMode.SELECT -> {
                    selectItemIndices.forEachIndexed { optionIndex, itemIndex ->
                        set(
                            itemIndex,
                            FillBlanksItem.Select(
                                id = itemIndex,
                                selectedOptionIndex = getSelectedOptionIndex(
                                    replyBlanks = replyBlanks,
                                    blankOptions = cachedOptions,
                                    optionIndex = optionIndex
                                )
                            )
                        )
                    }
                }
            }
        }

    private fun getInputText(
        replyBlanks: List<String>?,
        inputComponents: List<Component>,
        inputIndex: Int
    ): String? =
        replyBlanks?.getOrNull(inputIndex)
            ?: inputComponents.getOrNull(inputIndex)?.text

    private fun getSelectedOptionIndex(
        replyBlanks: List<String>?,
        blankOptions: List<FillBlanksOption>,
        optionIndex: Int
    ): Int? {
        val replyOption = replyBlanks?.getOrNull(optionIndex) ?: return null
        return blankOptions
            .indexOfFirst { it.originalText == replyOption }
            .takeIf { it != -1 }
    }

    private fun parseLanguage(langClass: String): String? =
        langClass
            .trimIndent()
            .removeSurrounding(LANGUAGE_CLASS_PREFIX, "\"")
            .takeIf { it.isNotEmpty() }

    private fun splitContent(
        content: String,
        produceInputItem: (id: Int, inputIndex: Int) -> FillBlanksItem.Input,
        produceSelectItem: (id: Int, optionIndex: Int) -> FillBlanksItem.Select
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
            var blankIndex = 0
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
                    when (mode) {
                        FillBlanksMode.INPUT -> {
                            add(produceInputItem(id++, blankIndex++))
                        }
                        FillBlanksMode.SELECT -> {
                            add(produceSelectItem(id++, blankIndex++))
                        }
                    }
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

    private fun getBlankOptions(blanksComponents: List<Component>): List<FillBlanksOption> =
        when (mode) {
            FillBlanksMode.INPUT -> emptyList()
            FillBlanksMode.SELECT -> {
                val blankOptions = blanksComponents.first().options ?: emptyList()
                blankOptions.map(::mapBlankOption)
            }
        }

    private fun mapBlankOption(originalText: String): FillBlanksOption {
        var displayText = originalText

        val match = blankOptionRegex.find(originalText)
        if (match != null) {
            val (option) = match.destructured
            displayText = option
        }

        return FillBlanksOption(
            originalText = originalText,
            displayText = displayText
        )
    }
}
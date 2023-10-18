package org.hyperskill.step_quiz

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.step_quiz.domain.model.attempts.Component
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksItemMapper

class FillBlanksMapperTest {
    /* ktlint-disable */
    companion object {
        private const val LANGUAGE_NAME = "typescript"
        private const val CONTENT = "Mark the following function's return type as string:\n\ndef function1() ▭:\n    return \"This function should return a string!\" \n\nMark the following function's return type as a set of floats:\n\ndef function2() ▭:\n    return {1, 2, 3, 4} "
        private const val TEXT = "<p>def function1() [...]:<br></p><pre><code class=\"language-$LANGUAGE_NAME\">$CONTENT</code></pre>"
    }

    private fun expectedItems(firstReply: String? = null, secondReply: String? = null) =
        listOf(
            FillBlanksItem.Text(0, "Mark the following function's return type as string:", false),
            FillBlanksItem.Text(1, "", true),
            FillBlanksItem.Text(2, "def function1() ", true),
            FillBlanksItem.Input(3, firstReply),
            FillBlanksItem.Text(4, ":", startsWithNewLine = false),
            FillBlanksItem.Text(5, "    return \"This function should return a string!\" ", true),
            FillBlanksItem.Text(6, text = "", startsWithNewLine = true),
            FillBlanksItem.Text(
                7,
                text = "Mark the following function's return type as a set of floats:",
                startsWithNewLine = true
            ),
            FillBlanksItem.Text(8, text = "", startsWithNewLine = true),
            FillBlanksItem.Text(9, text = "def function2() ", startsWithNewLine = true),
            FillBlanksItem.Input(10, secondReply),
            FillBlanksItem.Text(11, ":", startsWithNewLine = false),
            FillBlanksItem.Text(12,"    return {1, 2, 3, 4} ", startsWithNewLine = true)
        )

    @Test
    fun `FillBlanksMapper should correctly split text`() {
        val result = FillBlanksItemMapper.mapInternal(
            componentsDataset = listOf(
                Component(
                    type = Component.Type.TEXT,
                    text = TEXT
                ),
                Component(
                    type = Component.Type.INPUT
                ),
                Component(
                    type = Component.Type.INPUT
                )
            ),
            replyBlanks = null
        )
        assertEquals(expectedItems(), result?.fillBlanks)
    }

    @Test
    fun `FillBlanksMapper should use reply for inputs`() {
        val firstReply = "1"
        val secondReply = "2"
        val result = FillBlanksItemMapper.mapInternal(
            componentsDataset = listOf(
                Component(
                    type = Component.Type.TEXT,
                    text = TEXT
                ),
                Component(
                    type = Component.Type.INPUT
                ),
                Component(
                    type = Component.Type.INPUT
                )
            ),
            replyBlanks = listOf(firstReply, secondReply)
        )
        assertEquals(
            expectedItems(firstReply, secondReply),
            result?.fillBlanks
        )
    }

    @Test
    fun `FillBlanksMapper should extract language name from the CODE tag`() {
        val result = FillBlanksItemMapper.mapInternal(
            componentsDataset = listOf(
                Component(
                    type = Component.Type.TEXT,
                    text = TEXT
                )
            ),
            replyBlanks = null
        )
        assertEquals(LANGUAGE_NAME, result?.language)
    }
}
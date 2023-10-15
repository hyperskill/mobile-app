package org.hyperskill.step_quiz

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.attempts.Component
import org.hyperskill.app.step_quiz.domain.model.attempts.Dataset
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksItemMapper
import org.hyperskill.step_quiz.domain.model.stub

class FillBlanksMapperTest {
    companion object {
        private val TEXT = "<p>def function1() [...]:<br></p><pre><code class=\"language-typescript\">Mark the following function's return type as string:\\n\\ndef function1() ▭:\\n    return \"This function should return a string!\" \\n\\nMark the following function's return type as a set of floats:\\n\\ndef function2() ▭:\\n    return {1, 2, 3, 4} </code></pre>"
        private const val FIRST_TEXT_PART = "Mark the following function's return type as string:\\n\\ndef function1() "
        private const val SECOND_TEXT_PART = ":\\n    return \"This function should return a string!\" \\n\\nMark the following function's return type as a set of floats:\\n\\ndef function2() "
        private const val THIRD_TEXT_PART = ":\\n    return {1, 2, 3, 4} "
    }

    @Test
    fun `FillBlanksMapper should correctly split text`() {
        val result = FillBlanksItemMapper.map(
            Attempt.Companion.stub(
                dataset = Dataset(
                    components = listOf(
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
                    )
                )
            ),
            Submission()
        )
        val expectedResult = listOf(
            FillBlanksItem.Text(FIRST_TEXT_PART),
            FillBlanksItem.Input(null),
            FillBlanksItem.Text(SECOND_TEXT_PART),
            FillBlanksItem.Input(null),
            FillBlanksItem.Text(THIRD_TEXT_PART)
        )
        assertEquals(expectedResult, result)
    }

    @Test
    fun `FillBlanksMapper should use reply for inputs`() {
        val result = FillBlanksItemMapper.map(
            Attempt.Companion.stub(
                dataset = Dataset(
                    components = listOf(
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
                    )
                )
            ),
            Submission(
                reply = Reply(
                    blanks = listOf("1", "2")
                )
            )
        )
        val expectedResult = listOf(
            FillBlanksItem.Text(FIRST_TEXT_PART),
            FillBlanksItem.Input("1"),
            FillBlanksItem.Text(SECOND_TEXT_PART),
            FillBlanksItem.Input("2"),
            FillBlanksItem.Text(THIRD_TEXT_PART)
        )
        assertEquals(expectedResult, result)
    }
}
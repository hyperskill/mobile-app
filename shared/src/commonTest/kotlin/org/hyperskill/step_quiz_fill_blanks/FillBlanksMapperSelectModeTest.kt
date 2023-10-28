package org.hyperskill.step_quiz_fill_blanks

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.step_quiz.domain.model.attempts.Component
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksItem
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksOption
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksItemMapper

class FillBlanksMapperSelectModeTest {
    /* ktlint-disable */
    companion object {
        private const val LANGUAGE_NAME = "python"
        private const val CONTENT =
            "#...\nclass Role(db.Model):    \n    id = db.Column(db.Integer, primary_key = True)\n    name = db.Column(db.String(20), unique = True)\n    permissions = db.Column(db.Integer)\n    ▭           \n    \n    #...\n\nclass User(db.Model):\n    id = db.Column(db.Integer, primary_key = True) \n    username = db.Column(db.String(20), unique = True)\n    ▭    "
        private const val TEXT = "<pre><code class=\"language-$LANGUAGE_NAME\">$CONTENT</code></pre>"

        private val OPTIONS = listOf(
            "<code>users = db.relationship(\"User\", backref = \"role\")</code>",
            "role_id = db.Column(db.Integer, db.ForeignKey(\"role.id\"))",
            "<code>user_id = db.Column(db.Integer, db.ForeignKey(\"user.id\"))</code>",
            "roles = db.relationship(\"User\", backref = \"user)\""
        )
        private val EXPECTED_OPTIONS = listOf(
            FillBlanksOption(
                originalText = "<code>users = db.relationship(\"User\", backref = \"role\")</code>",
                displayText = "users = db.relationship(\"User\", backref = \"role\")"
            ),
            FillBlanksOption(
                originalText = "role_id = db.Column(db.Integer, db.ForeignKey(\"role.id\"))",
                displayText = "role_id = db.Column(db.Integer, db.ForeignKey(\"role.id\"))"
            ),
            FillBlanksOption(
                originalText = "<code>user_id = db.Column(db.Integer, db.ForeignKey(\"user.id\"))</code>",
                displayText = "user_id = db.Column(db.Integer, db.ForeignKey(\"user.id\"))"
            ),
            FillBlanksOption(
                originalText = "roles = db.relationship(\"User\", backref = \"user)\"",
                displayText = "roles = db.relationship(\"User\", backref = \"user)\""
            )
        )
    }

    private fun expectedItems(firstReply: Int? = null, secondReply: Int? = null) =
        listOf(
            FillBlanksItem.Text(0, "#...", false),
            FillBlanksItem.Text(1, "class Role(db.Model):    ", true),
            FillBlanksItem.Text(2, "&nbsp;&nbsp;&nbsp;&nbsp;id = db.Column(db.Integer, primary_key = True)", true),
            FillBlanksItem.Text(3, "&nbsp;&nbsp;&nbsp;&nbsp;name = db.Column(db.String(20), unique = True)", true),
            FillBlanksItem.Text(
                4,
                "&nbsp;&nbsp;&nbsp;&nbsp;permissions = db.Column(db.Integer)",
                startsWithNewLine = true
            ),
            FillBlanksItem.Text(5, "&nbsp;&nbsp;&nbsp;&nbsp;", true),
            FillBlanksItem.Select(6, selectedOptionIndex = firstReply),
            FillBlanksItem.Text(
                7,
                text = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
                startsWithNewLine = false
            ),
            FillBlanksItem.Text(8, text = "&nbsp;&nbsp;&nbsp;&nbsp;", startsWithNewLine = true),
            FillBlanksItem.Text(9, text = "&nbsp;&nbsp;&nbsp;&nbsp;#...", startsWithNewLine = true),
            FillBlanksItem.Text(10, text = "", startsWithNewLine = true),
            FillBlanksItem.Text(11, text = "class User(db.Model):", startsWithNewLine = true),
            FillBlanksItem.Text(
                12,
                text = "&nbsp;&nbsp;&nbsp;&nbsp;id = db.Column(db.Integer, primary_key = True) ",
                startsWithNewLine = true
            ),
            FillBlanksItem.Text(
                id = 13,
                text = "&nbsp;&nbsp;&nbsp;&nbsp;username = db.Column(db.String(20), unique = True)",
                true
            ),
            FillBlanksItem.Text(id = 14, text = "&nbsp;&nbsp;&nbsp;&nbsp;", true),
            FillBlanksItem.Select(id = 15, selectedOptionIndex = secondReply),
            FillBlanksItem.Text(id = 16, text = "&nbsp;&nbsp;&nbsp;&nbsp;", false)
        )

    @Test
    fun `FillBlanksMapper should correctly split text`() {
        val result = FillBlanksItemMapper(FillBlanksMode.SELECT).map(
            componentsDataset = listOf(
                Component(
                    type = Component.Type.TEXT,
                    text = TEXT
                ),
                Component(
                    type = Component.Type.SELECT,
                    options = OPTIONS
                ),
                Component(
                    type = Component.Type.SELECT,
                    options = OPTIONS
                )
            ),
            replyBlanks = null
        )

        assertEquals(expectedItems(), result?.fillBlanks)
        assertEquals(LANGUAGE_NAME, result?.language)
        assertEquals(EXPECTED_OPTIONS, result?.options)
    }

    @Test
    fun `FillBlanksMapper should use reply for selects`() {
        val firstSelectedOptionIndex = 2
        val secondSelectedOptionIndex = 0

        val result = FillBlanksItemMapper(FillBlanksMode.SELECT).map(
            componentsDataset = listOf(
                Component(
                    type = Component.Type.TEXT,
                    text = TEXT
                ),
                Component(
                    type = Component.Type.SELECT,
                    options = OPTIONS
                ),
                Component(
                    type = Component.Type.SELECT,
                    options = OPTIONS
                )
            ),
            replyBlanks = listOf(OPTIONS[firstSelectedOptionIndex], OPTIONS[secondSelectedOptionIndex])
        )
        assertEquals(
            expectedItems(firstSelectedOptionIndex, secondSelectedOptionIndex),
            result?.fillBlanks
        )
    }

    @Test
    fun `Second call to the FillBlanksMapper should return correct result`() {
        val mapper = FillBlanksItemMapper(FillBlanksMode.SELECT)
        val components = listOf(
            Component(
                type = Component.Type.TEXT,
                text = TEXT
            ),
            Component(
                type = Component.Type.SELECT,
                options = OPTIONS
            ),
            Component(
                type = Component.Type.SELECT,
                options = OPTIONS
            )
        )
        mapper.map(
            componentsDataset = components,
            replyBlanks = listOf(OPTIONS[0], OPTIONS[1])
        )

        val actualResult = mapper.map(
            componentsDataset = components,
            replyBlanks = listOf(OPTIONS[1], OPTIONS[2])
        )
        assertEquals(
            expectedItems(1, 2),
            actualResult?.fillBlanks
        )
    }
}
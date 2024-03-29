package org.hyperskill.step_quiz_fill_blanks

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.hyperskill.app.step_quiz.domain.model.attempts.Component
import org.hyperskill.app.step_quiz.domain.model.attempts.Dataset
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksConfig
import org.hyperskill.app.step_quiz_fill_blanks.model.InvalidFillBlanksConfigException
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksResolver

class FillBlanksResolverTest {

    companion object {
        private val TWO_BLANKS_TEXT =
            """
                Begin of the text ${FillBlanksConfig.BLANK_FIELD_CHAR}
                and the rest ${FillBlanksConfig.BLANK_FIELD_CHAR}
            """.trimIndent()
    }

    @Test
    fun `Data set with empty components must fail resolving`() {
        assertResolvingFailed(
            Dataset(components = emptyList())
        )
    }

    @Test
    fun `The first component should be a TEXT component`() {
        listOf(
            Component.Type.INPUT,
            Component.Type.SELECT
        ).forEach { componentType ->
            assertResolvingFailed(
                getComponentsDataSet(componentType)
            )
        }
    }

    @Test
    fun `All components except of the first one should be of type INPUT`() {
        listOf(
            Component.Type.TEXT,
            Component.Type.SELECT
        ).forEach { componentType ->
            assertResolvingFailed(
                getComponentsDataSet(Component.Type.TEXT, componentType)
            )
        }
    }

    @Test
    fun `Number of blanks in text component should be equal to number of components of type INPUT`() {
        val correctTextComponent = Component(
            type = Component.Type.TEXT,
            text = TWO_BLANKS_TEXT
        )

        listOf(
            Component.Type.INPUT,
            Component.Type.SELECT
        ).forEach { wrongType ->
            val wrongDataSet = Dataset(
                components = listOf(
                    correctTextComponent,
                    Component(type = wrongType)
                )
            )
            assertResolvingFailed(wrongDataSet)
        }

        assertResolvingPassed(
            Dataset(
                components = listOf(
                    correctTextComponent,
                    Component(type = Component.Type.INPUT),
                    Component(type = Component.Type.INPUT)
                )
            )
        )
    }

    @Test
    fun `All components of type SELECT must have the same options`() {
        val correctTextComponent = Component(
            type = Component.Type.TEXT,
            text = TWO_BLANKS_TEXT
        )

        Dataset(
            components = listOf(
                correctTextComponent,
                Component(
                    type = Component.Type.SELECT,
                    options = listOf("1", "2")
                ),
                Component(
                    type = Component.Type.SELECT,
                    options = listOf("2", "1")
                )
            )
        ).let(::assertResolvingPassed)

        Dataset(
            components = listOf(
                correctTextComponent,
                Component(
                    type = Component.Type.SELECT,
                    options = listOf("1", "2")
                ),
                Component(
                    type = Component.Type.SELECT,
                    options = listOf("1")
                )
            )
        ).let(::assertResolvingFailed)
    }

    @Test
    fun `Number of options in SELECT component must be greater or equal to number of blanks`() {
        val correctTextComponent = Component(
            type = Component.Type.TEXT,
            text = TWO_BLANKS_TEXT
        )

        Dataset(
            components = listOf(
                correctTextComponent,
                Component(
                    type = Component.Type.SELECT,
                    options = listOf("1")
                ),
                Component(
                    type = Component.Type.SELECT,
                    options = listOf("1")
                )
            )
        ).let(::assertResolvingFailed)

        Dataset(
            components = listOf(
                correctTextComponent,
                Component(
                    type = Component.Type.SELECT,
                    options = listOf("1", "1")
                ),
                Component(
                    type = Component.Type.SELECT,
                    options = listOf("1", "1")
                )
            )
        ).let(::assertResolvingPassed)
    }

    private fun getComponentsDataSet(vararg componentTypes: Component.Type): Dataset =
        Dataset(
            components = componentTypes.map { Component(type = it) }
        )

    private fun assertResolvingFailed(dataset: Dataset) {
        assertFailsWith<InvalidFillBlanksConfigException> {
            FillBlanksResolver.resolve(dataset)
        }
    }

    private fun assertResolvingPassed(dataset: Dataset) {
        assertTrue {
            FillBlanksResolver.resolve(dataset)
            true
        }
    }
}
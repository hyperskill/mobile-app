package org.hyperskill.app.step_quiz_fill_blanks.presentation

import org.hyperskill.app.step_quiz.domain.model.attempts.Component
import org.hyperskill.app.step_quiz.domain.model.attempts.Dataset
import org.hyperskill.app.step_quiz_fill_blanks.model.InvalidFillBlanksConfigException
import ru.nobird.app.core.model.slice

object FillBlanksResolver {
    internal const val BLANK_FIELD_CHAR = '▭'

    @Throws(InvalidFillBlanksConfigException::class)
    fun resolve(dataset: Dataset) {
        if (dataset.components.isNullOrEmpty()) {
            throw InvalidFillBlanksConfigException("Components should not be empty")
        }

        val textComponent = dataset.components.first()
        if (textComponent.type != Component.Type.TEXT) {
            throw InvalidFillBlanksConfigException("First component must be of type \"text\"")
        }

        val blanksComponents = dataset.components.slice(from = 1)

        val isInputMode = blanksComponents.all { it.type == Component.Type.INPUT }
        if (!isInputMode) {
            throw InvalidFillBlanksConfigException("All components except the first must be of type \"input\"")
        }

        val blankFieldsCount = textComponent.text?.count { it == BLANK_FIELD_CHAR }
        if (blanksComponents.count() != blankFieldsCount) {
            throw InvalidFillBlanksConfigException(
                """Number of blanks \"$BLANK_FIELD_CHAR\" in text component 
                    must be equal to number of components of type \"select\" or \"input\"
                    """.trimMargin()
            )
        }
    }
}
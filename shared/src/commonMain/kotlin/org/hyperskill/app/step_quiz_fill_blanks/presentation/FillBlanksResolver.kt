package org.hyperskill.app.step_quiz_fill_blanks.presentation

import org.hyperskill.app.step_quiz.domain.model.attempts.Component
import org.hyperskill.app.step_quiz.domain.model.attempts.Dataset
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksConfig
import org.hyperskill.app.step_quiz_fill_blanks.model.InvalidFillBlanksConfigException
import ru.nobird.app.core.model.slice

object FillBlanksResolver {

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
        val isSelectMode = blanksComponents.all { it.type == Component.Type.SELECT }

        if (!isInputMode && !isSelectMode) {
            throw InvalidFillBlanksConfigException(
                "All components except the first must be of type \"select\" or \"input\""
            )
        }

        val blankFieldsCount = textComponent.text?.count { it == FillBlanksConfig.BLANK_FIELD_CHAR }
        if (blanksComponents.count() != blankFieldsCount) {
            throw InvalidFillBlanksConfigException(
                """Number of blanks \"$FillBlanksConfig.BLANK_FIELD_CHAR\" in text component 
                    must be equal to number of components of type \"select\" or \"input\"
                    """.trimMargin()
            )
        }

        if (isSelectMode) {
            val blankOptions = blanksComponents.first().options?.toSet() ?: emptySet()

            if (blankOptions.count() < blankFieldsCount) {
                throw InvalidFillBlanksConfigException(
                    """"Number of options in component of type \"select\" must be greater or equal to number of blanks 
                        \"$FillBlanksConfig.BLANK_FIELD_CHAR\" in text component"    
                    """.trimMargin()
                )
            }

            if (blanksComponents.any { it.options?.toSet() != blankOptions }) {
                throw InvalidFillBlanksConfigException(
                    "All components of type \"select\" must have the same options"
                )
            }
        }
    }
}
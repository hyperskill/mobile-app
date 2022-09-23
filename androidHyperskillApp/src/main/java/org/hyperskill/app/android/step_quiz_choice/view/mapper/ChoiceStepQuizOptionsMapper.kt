package org.hyperskill.app.android.step_quiz_choice.view.mapper

import org.hyperskill.app.android.step_quiz_choice.view.model.Choice

class ChoiceStepQuizOptionsMapper {
    fun mapChoices(options: List<String>, choices: List<Boolean>?, isQuizEnabled: Boolean): List<Choice> =
        options.mapIndexed { i, option ->
            Choice(
                option = option,
                isSelected = choices?.getOrNull(i) ?: false,
                isEnabled = isQuizEnabled
            )
        }
}
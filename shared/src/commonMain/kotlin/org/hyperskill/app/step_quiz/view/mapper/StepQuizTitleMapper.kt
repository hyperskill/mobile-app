package org.hyperskill.app.step_quiz.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.step.domain.model.BlockName

class StepQuizTitleMapper(
    private val resourceProvider: ResourceProvider
) {
    fun getStepQuizTitle(blockName: String, isMultipleChoice: Boolean?, isCheckbox: Boolean?): String? =
        when (blockName) {
            BlockName.CHOICE -> {
                if (isMultipleChoice == true)
                    resourceProvider.getString(SharedResources.strings.step_quiz_choice_multiple_choice_title)
                else
                    resourceProvider.getString(SharedResources.strings.step_quiz_choice_single_choice_title)
            }
            BlockName.CODE -> resourceProvider.getString(SharedResources.strings.step_quiz_code_title)
            BlockName.SQL -> resourceProvider.getString(SharedResources.strings.step_quiz_sql_title)
            BlockName.MATCHING -> resourceProvider.getString(SharedResources.strings.step_quiz_matching_title)
            BlockName.MATH -> resourceProvider.getString(SharedResources.strings.step_quiz_math_title)
            BlockName.NUMBER -> resourceProvider.getString(SharedResources.strings.step_quiz_number_title)
            BlockName.SORTING -> resourceProvider.getString(SharedResources.strings.step_quiz_sorting_title)
            BlockName.STRING -> resourceProvider.getString(SharedResources.strings.step_quiz_string_title)
            BlockName.TABLE -> {
                if (isCheckbox == true)
                    resourceProvider.getString(SharedResources.strings.step_quiz_table_multiple_choice_title)
                else
                    resourceProvider.getString(SharedResources.strings.step_quiz_table_single_choice_title)
            }
            else -> null
        }
}
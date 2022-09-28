package org.hyperskill.app.step_quiz.domain.validation

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step_quiz.domain.model.submissions.ChoiceAnswer
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import ru.nobird.app.core.model.safeCast

class StepQuizReplyValidator(private val resourceProvider: ResourceProvider) {
    companion object {
        private const val MINUS = "-\\\u002D\u00AD\u2012\u2013\u2014\u2015\u02D7"
        private const val PLUS = "+"
        private const val POINT = ",\\."
        private const val EXP = "eEеЕ"
        private const val NUMBER_VALIDATION_REGEX = "^[$MINUS$PLUS]?[0-9]*[$POINT]?[0-9]+([$EXP][$$MINUS$PLUS]?[0-9]+)?$"
    }

    fun validate(reply: Reply, stepBlockName: String): ReplyValidationResult {
        when (stepBlockName) {
            BlockName.CHOICE -> {
                val choices = reply.choices?.safeCast<List<ChoiceAnswer.Choice>>()

                if (choices.isNullOrEmpty() || choices.none { it.boolValue }) {
                    return ReplyValidationResult.Error(getErrorMessage(stepBlockName))
                }
            }
            BlockName.MATCHING, BlockName.SORTING -> if (reply.ordering.isNullOrEmpty()) {
                return ReplyValidationResult.Error(getErrorMessage(stepBlockName))
            }
            BlockName.MATH -> if (reply.formula.isNullOrEmpty()) {
                return ReplyValidationResult.Error(getErrorMessage(stepBlockName))
            }
            BlockName.NUMBER -> if (reply.number.isNullOrEmpty() || !reply.number.matches(NUMBER_VALIDATION_REGEX.toRegex())) {
                return ReplyValidationResult.Error(getErrorMessage(stepBlockName))
            }
            BlockName.STRING -> if (reply.text.isNullOrEmpty() || reply.files.isNullOrEmpty()) {
                return ReplyValidationResult.Error(getErrorMessage(stepBlockName))
            }
            BlockName.TABLE -> {
                val choices = reply.choices?.safeCast<List<ChoiceAnswer.Table>>()

                if (choices.isNullOrEmpty() || choices.any { it -> it.tableChoice.columns.none { it.answer } }) {
                    return ReplyValidationResult.Error(getErrorMessage(stepBlockName))
                }
            }
            BlockName.CODE, BlockName.SQL -> return ReplyValidationResult.Success
            else -> throw IllegalArgumentException("Unsupported block type = $stepBlockName")
        }

        return ReplyValidationResult.Success
    }

    private fun getErrorMessage(stepBlockName: String): String =
        when (stepBlockName) {
            BlockName.CHOICE ->
                resourceProvider.getString(SharedResources.strings.step_quiz_choice_empty_reply)
            BlockName.MATCHING ->
                resourceProvider.getString(SharedResources.strings.step_quiz_matching_invalid_reply)
            BlockName.SORTING ->
                resourceProvider.getString(SharedResources.strings.step_quiz_sorting_invalid_reply)
            BlockName.MATH, BlockName.STRING ->
                resourceProvider.getString(SharedResources.strings.step_quiz_text_empty_reply)
            BlockName.NUMBER ->
                resourceProvider.getString(SharedResources.strings.step_quiz_text_invalid_number_reply)
            BlockName.TABLE ->
                resourceProvider.getString(SharedResources.strings.step_quiz_table_empty_reply)
            else ->
                throw IllegalArgumentException("Unsupported block type = $stepBlockName")
        }
}
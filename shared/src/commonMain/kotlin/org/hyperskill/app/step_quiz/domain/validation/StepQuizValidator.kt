package org.hyperskill.app.step_quiz.domain.validation

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.step_quiz.domain.model.submissions.ChoiceAnswer
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature

class StepQuizValidator(private val resourceProvider: ResourceProvider) {

    companion object {
        private const val MINUS = "-\\\u002D\u00AD\u2012\u2013\u2014\u2015\u02D7"
        private const val PLUS = "+"
        private const val POINT = ",\\."
        private const val EXP = "eEеЕ"
        private const val NUMBER_VALIDATION_REGEX = "^[$MINUS$PLUS]?[0-9]*[$POINT]?[0-9]+([$EXP][$$MINUS$PLUS]?[0-9]+)?$"
    }

    fun validate(reply: Reply): StepQuizFeature.SubmissionValidationState {

        reply.choices?.let {
            if (!choicesAreValid(it)) {
                return StepQuizFeature.SubmissionValidationState.Error(
                    resourceProvider.getString(SharedResources.strings.step_quiz_choice_empty_reply)
                )
            }
        }

        reply.number?.let {
            if (!numberIsValid(it)) {
                return StepQuizFeature.SubmissionValidationState.Error(
                    resourceProvider.getString(SharedResources.strings.step_quiz_text_invalid_number_reply)
                )
            }
        }

        reply.text?.let {
            if (!textIsValid(it)) {
                return StepQuizFeature.SubmissionValidationState.Error(
                    resourceProvider.getString(SharedResources.strings.step_quiz_text_empty_reply)
                )
            }
        }

        return StepQuizFeature.SubmissionValidationState.Success
    }

    private fun choicesAreValid(choices: List<ChoiceAnswer>): Boolean =
        choices.isNotEmpty() && choices.any {
            return when (it) {
                is ChoiceAnswer.Choice -> it.boolValue
                is ChoiceAnswer.Table -> it.tableChoice.columns.isNotEmpty()
            }
        }

    private fun numberIsValid(number: String): Boolean =
        number.matches(NUMBER_VALIDATION_REGEX.toRegex())

    private fun textIsValid(text: String): Boolean =
        text.isNotEmpty()
}
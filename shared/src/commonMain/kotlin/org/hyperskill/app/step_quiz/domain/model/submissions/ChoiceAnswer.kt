package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.Serializable
import org.hyperskill.app.step_quiz.domain.serialization.choice_answer.ChoiceAnswerChoiceSerializer
import org.hyperskill.app.step_quiz.domain.serialization.choice_answer.ChoiceAnswerContentSerializer
import org.hyperskill.app.step_quiz.domain.serialization.choice_answer.ChoiceAnswerTableSerializer

@Serializable(with = ChoiceAnswerContentSerializer::class)
sealed interface ChoiceAnswer {
    @Serializable(with = ChoiceAnswerChoiceSerializer::class)
    data class Choice(val boolValue: Boolean) : ChoiceAnswer

    @Serializable(with = ChoiceAnswerTableSerializer::class)
    data class Table(val tableChoice: TableChoiceAnswer) : ChoiceAnswer
}
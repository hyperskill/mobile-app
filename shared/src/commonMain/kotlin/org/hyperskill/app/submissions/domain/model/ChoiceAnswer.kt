package org.hyperskill.app.submissions.domain.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.submissions.domain.serialization.choice_answer.ChoiceAnswerChoiceSerializer
import org.hyperskill.app.submissions.domain.serialization.choice_answer.ChoiceAnswerContentSerializer
import org.hyperskill.app.submissions.domain.serialization.choice_answer.ChoiceAnswerTableSerializer

@Serializable(with = ChoiceAnswerContentSerializer::class)
sealed interface ChoiceAnswer {
    @Serializable(with = ChoiceAnswerChoiceSerializer::class)
    data class Choice(val boolValue: Boolean) : ChoiceAnswer

    @Serializable(with = ChoiceAnswerTableSerializer::class)
    data class Table(val tableChoice: TableChoiceAnswer) : ChoiceAnswer
}
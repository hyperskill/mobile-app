package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.step_quiz.domain.serialization.feedback.FeedbackContentSerializer
import org.hyperskill.app.step_quiz.domain.serialization.feedback.FeedbackTextSerializer

@Serializable(with = FeedbackContentSerializer::class)
sealed interface Feedback {
    @Serializable(with = FeedbackTextSerializer::class)
    data class Text(val text: String) : Feedback

    @Serializable
    data class Object(
        @SerialName("message")
        val message: String
    ) : Feedback
}
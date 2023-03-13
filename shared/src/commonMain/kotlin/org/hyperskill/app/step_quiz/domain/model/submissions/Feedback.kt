package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.step_quiz.domain.serialization.feedback.FeedbackContentSerializer
import org.hyperskill.app.step_quiz.domain.serialization.feedback.FeedbackTextSerializer

// TODO: We should collect all available feedback types and create a single polymorphic serializer for them
@Serializable(with = FeedbackContentSerializer::class)
sealed interface Feedback {
    @Serializable(with = FeedbackTextSerializer::class)
    data class Text(val text: String) : Feedback

    @Serializable
    data class Object(
        @SerialName("title")
        val title: String = "",
        @SerialName("message")
        val message: String = ""
    ) : Feedback
}

fun Feedback.formattedText(): String =
    when (this) {
        is Feedback.Object -> {
            "$title\n\n$message".trim()
        }
        is Feedback.Text -> this.text
    }
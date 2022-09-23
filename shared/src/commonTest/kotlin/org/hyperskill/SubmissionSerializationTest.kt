package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus

class SubmissionSerializationTest {
    @Test
    fun submissionSerializationFeedbackTextTest() {
        val json = NetworkModule.provideJson()
        val encodedValue = json.encodeToJsonElement(
            Submission(
                id = 123L,
                status = SubmissionStatus.CORRECT,
                score = "1",
                hint = "Hint",
                time = "2017-07-13T16:59:00Z",
                reply = null,
                attempt = 200L,
//                feedback = Feedback.Text("This is a primitive")
            )
        )
        val expected = buildJsonObject {
            put("id", JsonPrimitive(123L))
            put("status", JsonPrimitive("correct"))
            put("score", JsonPrimitive("1"))
            put("hint", JsonPrimitive("Hint"))
            put("time", JsonPrimitive("2017-07-13T16:59:00Z"))
            put("attempt", JsonPrimitive(200L))
//            put("feedback", JsonPrimitive("This is a primitive"))
        }
        assertEquals(expected, encodedValue)
    }

    @Test
    fun submissionSerializationFeedbackObjectTest() {
        val json = NetworkModule.provideJson()
        val encodedValue = json.encodeToJsonElement(
            Submission(
                id = 123L,
                status = SubmissionStatus.CORRECT,
                score = "1",
                hint = "Hint",
                time = "2017-07-13T16:59:00Z",
                reply = null,
                attempt = 200L,
//                feedback = Feedback.Object("This is a message")
            )
        )
        val expected = buildJsonObject {
            put("id", JsonPrimitive(123L))
            put("status", JsonPrimitive("correct"))
            put("score", JsonPrimitive("1"))
            put("hint", JsonPrimitive("Hint"))
            put("time", JsonPrimitive("2017-07-13T16:59:00Z"))
            put("attempt", JsonPrimitive(200L))
//            putJsonObject("feedback") {
//                put("message", JsonPrimitive("This is a message"))
//            }
        }
        assertEquals(expected, encodedValue)
    }

    @Test
    fun submissionDeserializationFeedbackTextTest() {
        val json = NetworkModule.provideJson()
        val expected =
            Submission(
                id = 123L,
                status = SubmissionStatus.CORRECT,
                score = "1",
                hint = "Hint",
                time = "2017-07-13T16:59:00Z",
                reply = null,
                attempt = 200L,
//                feedback = Feedback.Text("This is a primitive")
            )
        val jsonObject = buildJsonObject {
            put("id", JsonPrimitive(123L))
            put("status", JsonPrimitive("correct"))
            put("score", JsonPrimitive("1"))
            put("hint", JsonPrimitive("Hint"))
            put("time", JsonPrimitive("2017-07-13T16:59:00Z"))
            put("attempt", JsonPrimitive(200L))
//            put("feedback", JsonPrimitive("This is a primitive"))
        }
        assertEquals(expected, json.decodeFromJsonElement(jsonObject))
    }

    @Test
    fun submissionDeserializationFeedbackObjectTest() {
        val json = NetworkModule.provideJson()
        val expected =
            Submission(
                id = 123L,
                status = SubmissionStatus.CORRECT,
                score = "1",
                hint = "Hint",
                time = "2017-07-13T16:59:00Z",
                reply = null,
                attempt = 200L,
//                feedback = Feedback.Object("This is a message")
            )
        val jsonObject = buildJsonObject {
            put("id", JsonPrimitive(123L))
            put("status", JsonPrimitive("correct"))
            put("score", JsonPrimitive("1"))
            put("hint", JsonPrimitive("Hint"))
            put("time", JsonPrimitive("2017-07-13T16:59:00Z"))
            put("attempt", JsonPrimitive(200L))
//            putJsonObject("feedback") {
//                put("message", JsonPrimitive("This is a message"))
//            }
        }
        assertEquals(expected, json.decodeFromJsonElement(jsonObject))
    }
}
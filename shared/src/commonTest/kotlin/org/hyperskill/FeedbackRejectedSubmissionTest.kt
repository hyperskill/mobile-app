package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.step_quiz.domain.model.submissions.FeedbackRejectedSubmission

class FeedbackRejectedSubmissionTest {
    @Test
    fun feedbackRejectedSubmissionSerializationTest() {
        val json = NetworkModule.provideJson()
        val encodedValue = json.encodeToJsonElement(FeedbackRejectedSubmission("title", "message"))
        val expected = buildJsonObject {
            put("title", JsonPrimitive("title"))
            put("message", JsonPrimitive("message"))
        }
        assertEquals(expected, encodedValue)
    }

    @Test
    fun feedbackRejectedSubmissionDeserializationTest() {
        val json = NetworkModule.provideJson()
        val expected = json.encodeToJsonElement(FeedbackRejectedSubmission("title", "message"))
        val jsonObject = buildJsonObject {
            put("title", JsonPrimitive("title"))
            put("message", JsonPrimitive("message"))
        }
        assertEquals(expected, json.decodeFromJsonElement(jsonObject))
    }

    @Test
    fun feedbackRejectedSubmissionDeserializationHasDefaultValuesTest() {
        val json = NetworkModule.provideJson()
        val expected = FeedbackRejectedSubmission("", "")
        val jsonObject = buildJsonObject {
            put("text", JsonPrimitive("title"))
            put("description", JsonPrimitive("message"))
        }
        assertEquals(expected, json.decodeFromJsonElement(jsonObject))
    }
}
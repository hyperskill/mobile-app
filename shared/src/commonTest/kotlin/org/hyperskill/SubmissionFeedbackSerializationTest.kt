package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.step_quiz.domain.model.submissions.Feedback

class SubmissionFeedbackSerializationTest {

    @kotlinx.serialization.Serializable
    private class TestFeedbackStringSerialization(val feedback: Feedback)

    @Test
    fun `Test submission feedback serialization to object`() {
        val json = NetworkModule.provideJson()
        val encodedValue = json.encodeToJsonElement(Feedback.Object("title", "message"))
        val expected = buildJsonObject {
            put("title", JsonPrimitive("title"))
            put("message", JsonPrimitive("message"))
        }
        assertEquals(expected, encodedValue)
    }

    @Test
    fun `Test submission feedback deserialization from object`() {
        val json = NetworkModule.provideJson()
        val expected = json.encodeToJsonElement(Feedback.Object("title", "message"))
        val jsonObject = buildJsonObject {
            put("title", JsonPrimitive("title"))
            put("message", JsonPrimitive("message"))
        }
        assertEquals(expected, json.decodeFromJsonElement(jsonObject))
    }

    @Test
    fun `Test deserialized submission feedback object has default values`() {
        val json = NetworkModule.provideJson()
        val expected = Feedback.Object("", "")
        val jsonObject = buildJsonObject {
            put("text", JsonPrimitive("title"))
            put("description", JsonPrimitive("message"))
        }
        assertEquals(expected, json.decodeFromJsonElement(jsonObject))
    }

    @Test
    fun `Test feedback string deserializing`() {
        val json = NetworkModule.provideJson()
        val expected = Feedback.Text("")
        val jsonObject = buildJsonObject {
            put("feedback", JsonPrimitive(""))
        }
        val decodedObject = json.decodeFromJsonElement(
            TestFeedbackStringSerialization.serializer(),
            jsonObject
        )
        assertEquals(expected, decodedObject.feedback)
    }
}
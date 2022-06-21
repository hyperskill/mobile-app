package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.step_quiz.domain.model.submissions.Cell
import org.hyperskill.app.step_quiz.domain.model.submissions.ChoiceAnswer
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.TableChoiceAnswer

class ReplySerializationTest {
    @Test
    fun replySerializationChoiceReplyTest() {
        val json = NetworkModule.provideJson()
        val encodedValue = json.encodeToJsonElement(
            Reply(
                choices = listOf(
                    ChoiceAnswer.Choice(false),
                    ChoiceAnswer.Choice(true),
                    ChoiceAnswer.Choice(false),
                    ChoiceAnswer.Choice(false)
                )
            )
        )
        val expected = buildJsonObject {
            put(
                "choices",
                JsonArray(
                    listOf(
                        JsonPrimitive(false),
                        JsonPrimitive(true),
                        JsonPrimitive(false),
                        JsonPrimitive(false)
                    )
                )
            )
        }
        assertEquals(expected, encodedValue)
    }

    @Test
    fun replySerializationTableReplyTest() {
        val json = NetworkModule.provideJson()
        val encodedValue = json.encodeToJsonElement(
            Reply(
                choices = listOf(
                    ChoiceAnswer.Table(
                        TableChoiceAnswer(
                            "incorrect keyword",
                            listOf(
                                Cell("compile-time", true),
                                Cell("run-time", false)
                            )
                        )
                    ),
                    ChoiceAnswer.Table(
                        TableChoiceAnswer(
                            "incorrect text output",
                            listOf(
                                Cell("compile-time", false),
                                Cell("run-time", true)
                            )
                        )
                    )
                )
            )
        )
        val expected = buildJsonObject {
            put(
                "choices",
                JsonArray(
                    listOf(
                        JsonObject(
                            mapOf(
                                "name_row" to JsonPrimitive("incorrect keyword"),
                                "columns" to JsonArray(
                                    listOf(
                                        JsonObject(
                                            mapOf(
                                                "name" to JsonPrimitive("compile-time"),
                                                "answer" to JsonPrimitive(true)
                                            )
                                        ),
                                        JsonObject(
                                            mapOf(
                                                "name" to JsonPrimitive("run-time"),
                                                "answer" to JsonPrimitive(false)
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        JsonObject(
                            mapOf(
                                "name_row" to JsonPrimitive("incorrect text output"),
                                "columns" to JsonArray(
                                    listOf(
                                        JsonObject(
                                            mapOf(
                                                "name" to JsonPrimitive("compile-time"),
                                                "answer" to JsonPrimitive(false)
                                            )
                                        ),
                                        JsonObject(
                                            mapOf(
                                                "name" to JsonPrimitive("run-time"),
                                                "answer" to JsonPrimitive(true)
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
        assertEquals(expected, encodedValue)
    }
}
package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
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

    @Test
    fun replySerializationPyCharmReplyTest() {
        val step = Step(
            id = 1,
            title = "",
            type = Step.Type.PRACTICE,
            block = Block(
                name = "pycharm",
                text = "",
                options = Block.Options(
                    files = listOf(
                        Block.Options.File(
                            name = "src/Zookeeper.kt",
                            isVisible = true,
                            text = "test visible text"
                        ),
                        Block.Options.File(
                            name = "test/ZookeeperTest.kt",
                            isVisible = false,
                            text = "test hidden text"
                        )
                    )
                )
            ),
            commentsStatistics = emptyList(),
            solvedBy = 0,
            isCompleted = false,
            isNext = false,
            canSkip = false,
            secondsToComplete = null,
            checkProfile = "hyperskill_java"
        )

        val json = NetworkModule.provideJson()

        val encodedValue = json.encodeToJsonElement(
            Reply.pycharm(step, "pycharm code")
        )
        val expected = buildJsonObject {
            put("score", JsonPrimitive(""))
            put(
                "solution",
                JsonArray(
                    listOf(
                        JsonObject(
                            mapOf(
                                "name" to JsonPrimitive("src/Zookeeper.kt"),
                                "is_visible" to JsonPrimitive(true),
                                "text" to JsonPrimitive("pycharm code")
                            )
                        ),
                        JsonObject(
                            mapOf(
                                "name" to JsonPrimitive("test/ZookeeperTest.kt"),
                                "is_visible" to JsonPrimitive(false),
                                "text" to JsonPrimitive("test hidden text")
                            )
                        )
                    )
                )
            )
            put("check_profile", JsonPrimitive("hyperskill_java"))
        }
        assertEquals(expected, encodedValue)
    }
}
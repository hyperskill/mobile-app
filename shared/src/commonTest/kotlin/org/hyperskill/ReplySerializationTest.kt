package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.submissions.domain.model.Cell
import org.hyperskill.app.submissions.domain.model.ChoiceAnswer
import org.hyperskill.app.submissions.domain.model.PyCharmFile
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.ReplyScore
import org.hyperskill.app.submissions.domain.model.TableChoiceAnswer

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
            isCribbed = false,
            isNext = false,
            canSkip = false,
            secondsToComplete = null,
            successRate = null,
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

    @Test
    fun `PyCharm Reply deserialization`() {
        val expectedReply = Reply(
            score = ReplyScore.String(""),
            solution = listOf(
                PyCharmFile(
                    name = "src/Zookeeper.kt",
                    isVisible = true,
                    text = "pycharm code"
                ),
                PyCharmFile(
                    name = "test/ZookeeperTest.kt",
                    isVisible = false,
                    text = "test hidden text"
                )
            )
        )
        val decodedReply = NetworkModule.provideJson().decodeFromString(
            Reply.serializer(),
            """
{
    "score": "",
    "solution":
    [
        {
            "name": "src/Zookeeper.kt",
            "is_visible": true,
            "text": "pycharm code"
        },
        {
            "name": "test/ZookeeperTest.kt",
            "is_visible": false,
            "text": "test hidden text"
        }
    ]
}
            """.trimIndent()
        )
        assertEquals(expectedReply, decodedReply)
    }

    @Test
    fun `Prompt Reply serialization with score`() {
        val json = NetworkModule.provideJson()
        val encodedValue = json.encodeToJsonElement(
            Reply.prompt(
                prompt = "prompt",
                markedAsCorrect = true
            )
        )
        val expected = buildJsonObject {
            put("prompt", JsonPrimitive("prompt"))
            put("score", JsonPrimitive(1.0))
        }
        assertEquals(expected, encodedValue)
    }

    @Test
    fun `Prompt Reply serialization without score`() {
        val json = NetworkModule.provideJson()
        val encodedValue = json.encodeToJsonElement(
            Reply.prompt(
                prompt = "prompt",
                markedAsCorrect = false
            )
        )
        val expected = buildJsonObject {
            put("prompt", JsonPrimitive("prompt"))
        }
        assertEquals(expected, encodedValue)
    }

    @Test
    fun `Prompt Reply deserialization with score`() {
        val expectedReply = Reply.prompt(
            prompt = "prompt",
            markedAsCorrect = true
        )
        val decodedReply = NetworkModule.provideJson().decodeFromString(
            Reply.serializer(),
            """
{
    "prompt": "prompt",
    "score": 1
}
            """.trimIndent()
        )
        assertEquals(expectedReply, decodedReply)
    }

    @Test
    fun `Prompt Reply deserialization without score`() {
        val expectedReply = Reply.prompt(
            prompt = "prompt",
            markedAsCorrect = false
        )
        val decodedReply = NetworkModule.provideJson().decodeFromString(
            Reply.serializer(),
            "{ \"prompt\": \"prompt\" }"
        )
        assertEquals(expectedReply, decodedReply)
    }

    @Test
    fun `Code Reply serialization with nullable lint_profile`() {
        val json = NetworkModule.provideJson()
        val encodedValue = json.encodeToJsonElement(
            Reply.code(
                code = "code",
                language = "language"
            )
        )
        val expected = buildJsonObject {
            put("code", JsonPrimitive("code"))
            put("language", JsonPrimitive("language"))
            put("lint_profile", JsonNull)
        }
        assertEquals(expected, encodedValue)
    }

    @Test
    fun `Code Reply serialization with provided lint_profile`() {
        val json = NetworkModule.provideJson()
        val encodedValue = json.encodeToJsonElement(
            Reply.code(
                code = "code",
                language = "language",
                lintProfile = "lint_profile"
            )
        )
        val expected = buildJsonObject {
            put("code", JsonPrimitive("code"))
            put("language", JsonPrimitive("language"))
            put("lint_profile", JsonPrimitive("lint_profile"))
        }
        assertEquals(expected, encodedValue)
    }

    @Test
    fun `Code Reply serialization with default lint_profile`() {
        val json = NetworkModule.provideJson()
        val encodedValue = json.encodeToJsonElement(
            Reply.code(
                code = "code",
                language = "language",
                lintProfile = ""
            )
        )
        val expected = buildJsonObject {
            put("code", JsonPrimitive("code"))
            put("language", JsonPrimitive("language"))
        }
        assertEquals(expected, encodedValue)
    }
}
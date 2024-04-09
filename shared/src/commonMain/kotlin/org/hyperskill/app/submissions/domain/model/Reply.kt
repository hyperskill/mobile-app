package org.hyperskill.app.submissions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.step.domain.model.Step

/**
 * TODO: replace with a sealed hierarchy on mobile side to avoid invalid Reply state
 */
@Serializable
data class Reply(
    @SerialName("choices")
    val choices: List<ChoiceAnswer>? = null,
    @SerialName("text")
    val text: String? = null,
    @SerialName("attachments")
    val attachments: List<Attachment>? = null,
    @SerialName("formula")
    val formula: String? = null,
    @SerialName("number")
    val number: String? = null,
    @SerialName("ordering")
    val ordering: List<Int>? = null,
    @SerialName("language")
    val language: String? = null,
    @SerialName("code")
    val code: String? = null,
    @SerialName("blanks")
    val blanks: List<String>? = null,
    @SerialName("files")
    val files: List<String>? = null,
    @SerialName("solve_sql")
    val solveSql: String? = null,
    @SerialName("score")
    val score: ReplyScore? = null,
    @SerialName("solution")
    val solution: List<PyCharmFile>? = null,
    @SerialName("check_profile")
    val checkProfile: String? = null,
    @SerialName("lines")
    val lines: List<ParsonsLine>? = null,
    @SerialName("prompt")
    val prompt: String? = null
) {

    companion object {
        internal const val PROMPT_MANUALLY_CONFIRMED_SCORE: Float = 1F

        fun code(code: String?, language: String?): Reply =
            Reply(code = code, language = language)

        fun sql(sqlCode: String?): Reply =
            Reply(solveSql = sqlCode)

        fun pycharm(step: Step, pycharmCode: String?): Reply =
            Reply(
                score = ReplyScore.String(""),
                solution = step.block.options.files?.map { file ->
                    PyCharmFile(
                        name = file.name,
                        isVisible = file.isVisible,
                        text = if (file.isVisible) pycharmCode ?: "" else file.text
                    )
                },
                checkProfile = step.checkProfile
            )

        fun parsons(lines: List<ParsonsLine>): Reply =
            Reply(lines = lines)

        fun fillBlanks(blanks: List<String>): Reply =
            Reply(blanks = blanks)

        fun prompt(prompt: String, markedAsCorrect: Boolean): Reply =
            Reply(
                prompt = prompt,
                score = if (markedAsCorrect) ReplyScore.Float(PROMPT_MANUALLY_CONFIRMED_SCORE) else null
            )
    }
}

fun Reply.pycharmCode(): String? =
    solution?.first { it.isVisible }?.text

fun Reply.isPromptForceScoreCheckboxChecked(): Boolean =
    score is ReplyScore.Float && score.floatValue == Reply.PROMPT_MANUALLY_CONFIRMED_SCORE
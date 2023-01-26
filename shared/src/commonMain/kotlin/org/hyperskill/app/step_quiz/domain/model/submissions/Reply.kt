package org.hyperskill.app.step_quiz.domain.model.submissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val solveSql: String? = null
) {

    companion object {
        fun sql(sqlCode: String?): Reply =
            Reply(solveSql = sqlCode)
    }
}

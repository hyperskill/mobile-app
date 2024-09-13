package org.hyperskill.app.step_quiz_code_blanks.domain.model.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CodeBlockTemplateEntryType {
    @SerialName("blank")
    BLANK,
    @SerialName("print")
    PRINT,
    @SerialName("variable")
    VARIABLE,
    @SerialName("if")
    IF,
    @SerialName("elif")
    ELIF,
    @SerialName("else")
    ELSE,

    UNKNOWN
}
package org.hyperskill.app.step_quiz_code_blanks.domain.model.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CodeBlockTemplateEntry(
    @SerialName("type")
    val type: CodeBlockTemplateEntryType = CodeBlockTemplateEntryType.UNKNOWN,
    @SerialName("indent_level")
    val indentLevel: Int = 0,
    @SerialName("is_active")
    val isActive: Boolean = false,
    @SerialName("delete_forbidden")
    val isDeleteForbidden: Boolean = false,
    @SerialName("children")
    val children: List<String> = emptyList()
)
package org.hyperskill.app.step.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Block(
    @SerialName("name")
    val name: String,
    @SerialName("text")
    val text: String,
    @SerialName("options")
    val options: Options,
    @SerialName("table_of_contents")
    val tableOfContents: List<TableOfContentsEntry>
) {
    @Serializable
    data class Options(
        @SerialName("is_multiple_choice")
        val isMultipleChoice: Boolean? = null,
        @SerialName("language")
        val language: String? = null,
        @SerialName("is_checkbox")
        val isCheckbox: Boolean? = null,
        @SerialName("execution_time_limit")
        val executionTimeLimit: Int? = null,
        @SerialName("execution_memory_limit")
        val executionMemoryLimit: Int? = null,
        @SerialName("limits")
        val limits: Map<String, Limit>? = null,
        @SerialName("code_templates")
        val codeTemplates: Map<String, String>? = null,
        @SerialName("samples")
        val samples: List<List<String>>? = null,
        @SerialName("files")
        val files: List<File>? = null
    ) {
        @Serializable
        data class File(
            @SerialName("name")
            val name: String,
            @SerialName("is_visible")
            val isVisible: Boolean,
            @SerialName("text")
            val text: String
        )
    }
}
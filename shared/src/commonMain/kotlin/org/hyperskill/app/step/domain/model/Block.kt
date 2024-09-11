package org.hyperskill.app.step.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.code.domain.model.ProgrammingLanguage

@Serializable
data class Block(
    @SerialName("name")
    val name: String,
    @SerialName("text")
    val text: String,
    @SerialName("options")
    val options: Options
) {
    @Serializable
    data class Options(
        @SerialName("is_multiple_choice")
        val isMultipleChoice: Boolean? = null,
        @SerialName("language")
        val language: String? = null,
        @SerialName("is_checkbox")
        val isCheckbox: Boolean? = null,
        @SerialName("limits")
        val limits: Map<String, Limit>? = null,
        @SerialName("code_templates")
        val codeTemplates: Map<String, String>? = null,
        @SerialName("samples")
        private val internalSamples: List<List<String>>? = null,
        @SerialName("files")
        val files: List<File>? = null,
        @SerialName("code_blanks_strings")
        val codeBlanksStrings: List<String>? = null,
        @SerialName("code_blanks_variables")
        val codeBlanksVariables: List<String>? = null,
        @SerialName("code_blanks_operations")
        val codeBlanksOperations: List<String>? = null,
        @SerialName("code_blanks_enabled")
        val codeBlanksEnabled: Boolean? = null
    ) {
        val samples: List<Sample>?
            get() = internalSamples?.mapNotNull {
                Sample(
                    input = it.firstOrNull() ?: return@mapNotNull null,
                    output = it.getOrNull(1) ?: return@mapNotNull null
                )
            }

        @Serializable
        data class File(
            @SerialName("name")
            val name: String,
            @SerialName("is_visible")
            val isVisible: Boolean,
            @SerialName("text")
            val text: String
        )

        data class Sample(
            val input: String,
            val output: String
        )
    }
}

val Block.Options.programmingLanguage: ProgrammingLanguage?
    get() = language?.let(ProgrammingLanguage::of)
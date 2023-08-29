package org.hyperskill.app.step_quiz.domain.model.attempts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dataset(
    @SerialName("options")
    val options: List<String>? = null,
    @SerialName("pairs")
    val pairs: List<Pair>? = null,
    @SerialName("rows")
    val rows: List<String>? = null,
    @SerialName("columns")
    val columns: List<String>? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("components")
    val components: List<Component>? = null,
    @SerialName("is_multiple_choice")
    val isMultipleChoice: Boolean = false,
    @SerialName("is_checkbox")
    val isCheckbox: Boolean = false,
    @SerialName("is_html_enabled")
    val isHtmlEnabled: Boolean = false,
    @SerialName("lines")
    val lines: List<String>? = null
)
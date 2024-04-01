package org.hyperskill.app.submissions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.nobird.app.core.model.Identifiable

@Serializable
data class Cell(
    @SerialName("name")
    override val id: String,
    @SerialName("answer")
    var answer: Boolean
) : Identifiable<String> {
    val name: String
        get() = id
}